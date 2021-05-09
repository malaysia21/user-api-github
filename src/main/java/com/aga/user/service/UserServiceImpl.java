package com.aga.user.service;

import com.aga.user.client.UserExternalClient;
import com.aga.user.exception.UserException;
import com.aga.user.exception.UserExceptionEnum;
import com.aga.user.mapper.UserMapper;
import com.aga.user.model.UserResponse;
import com.aga.user.model.github.User;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserExternalClient userExternalClient;
    private final UserMapper userMapper;
    private final UserRequestCounterUserService userRequestCounterUserService;

    public UserResponse getUserByLogin(String login) {
        User user = getUserFromExternalClient(login);
        UserResponse userResponse = userMapper.mapUserToUserResponse(user);
        userRequestCounterUserService.callUpdateRequestCounterFunction(login);
        return userResponse;
    }

    private User getUserFromExternalClient(String login) {
        try {
            log.debug("Sending request GET user by login: {}", login);
            User user =  userExternalClient.getUserByLogin(login).getBody();
            log.debug("Response GET user by login: {}", user);
            return user;
        } catch (FeignException ex) {
            log.error("Error processing request GET user by login: {} - {}", login, ex.getMessage());
            if (HttpStatus.NOT_FOUND.value() == ex.status()) {
                throw UserException.UserExceptionFactory.getException(UserExceptionEnum.USER_NOT_FOUND_EXCEPTION, login);
            }
            else {
                throw UserException.UserExceptionFactory.getException(UserExceptionEnum.USER_OTHER_EXCEPTION, ex.getMessage());
            }
        } catch (Exception ex) {
            log.error("Error processing request GET user by login: {} - {}", login, ex.getMessage());
            throw UserException.UserExceptionFactory.getException(UserExceptionEnum.USER_OTHER_EXCEPTION, ex.getMessage());
        }
    }
}
