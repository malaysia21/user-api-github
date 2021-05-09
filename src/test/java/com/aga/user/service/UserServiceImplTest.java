package com.aga.user.service;

import com.aga.user.client.UserExternalClient;
import com.aga.user.exception.UserException;
import com.aga.user.mapper.UserMapper;
import com.aga.user.model.UserResponse;
import com.aga.user.model.github.User;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Mock
    private UserExternalClient userExternalClient;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserRequestCounterUserService userRequestCounterUserService;

    @InjectMocks
    private UserServiceImpl userService;

    private static final String USER_LOGIN = "login";
    private static final String MESSAGE_FOR_INTERNAL_SERVER_ERROR = "Internal server error";

    @Test
    void getUserByLogin() {
        //given
        long userFollowers = 2;
        long userPublicRepo = 3;
        User user = User.builder().login(USER_LOGIN).followers(userFollowers).public_repos(userPublicRepo).build();

        //when
        Mockito.when(userExternalClient.getUserByLogin(anyString())).thenReturn(ResponseEntity.of(Optional.of(user)));
        UserResponse userResponse = userService.getUserByLogin(USER_LOGIN);


        //then
        verify(userExternalClient, times(1)).getUserByLogin(anyString());
        verify(userMapper, times(1)).mapUserToUserResponse(any(User.class));
        verify(userRequestCounterUserService, times(1)).callUpdateRequestCounterFunction(anyString());

        assertEquals(userResponse.getLogin(), USER_LOGIN);
        assertEquals(userResponse.getCalculations(), 6 / userFollowers * (2 + userPublicRepo));
    }

    @Test
    void getUserByLoginWithZeroFollowers() {
        //given
        long userFollowers = 0;
        long userPublicRepo = 3;
        User user = User.builder().login(USER_LOGIN).followers(userFollowers).public_repos(userPublicRepo).build();

        //when
        Mockito.when(userExternalClient.getUserByLogin(anyString())).thenReturn(ResponseEntity.of(Optional.of(user)));
        UserResponse userResponse = userService.getUserByLogin(USER_LOGIN);

        //then
        verify(userExternalClient, times(1)).getUserByLogin(anyString());
        verify(userMapper, times(1)).mapUserToUserResponse(any(User.class));
        verify(userRequestCounterUserService, times(1)).callUpdateRequestCounterFunction(anyString());

        assertEquals(userResponse.getLogin(), USER_LOGIN);
        assertEquals(userResponse.getCalculations(), 0);
    }

    @Test
    void getUserByLoginWithFeignExceptionNotFound() {
        //given
        FeignException feignException = buildFeignException(HttpStatus.NOT_FOUND);

        //when
        Mockito.when(userExternalClient.getUserByLogin(anyString())).thenThrow(feignException);

        UserException userException = assertThrows(UserException.class, () -> userService.getUserByLogin(USER_LOGIN));

        //then
        verify(userExternalClient, times(1)).getUserByLogin(anyString());
        verify(userMapper, never()).mapUserToUserResponse(any(User.class));
        verify(userRequestCounterUserService, never()).callUpdateRequestCounterFunction(anyString());

        assertEquals(userException.getHttpStatus(), HttpStatus.NOT_FOUND);
        assertTrue(userException.getMessage().contains(USER_LOGIN));
    }

    @Test
    void getUserByLoginWithFeignExceptionInternalServerError() {
        //given
        FeignException feignException = buildFeignException(HttpStatus.INTERNAL_SERVER_ERROR);

        //when
        Mockito.when(userExternalClient.getUserByLogin(anyString())).thenThrow(feignException);

        UserException userException = assertThrows(UserException.class, () -> userService.getUserByLogin(USER_LOGIN));

        //then
        verify(userExternalClient, times(1)).getUserByLogin(anyString());
        verify(userMapper, never()).mapUserToUserResponse(any(User.class));
        verify(userRequestCounterUserService, never()).callUpdateRequestCounterFunction(anyString());

        assertEquals(userException.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertTrue(userException.getMessage().contains(MESSAGE_FOR_INTERNAL_SERVER_ERROR));
    }

    @Test
    void getUserByLoginWithAnyException() {
        //given
        String exceptionMessage = "Exception";
        //when
        Mockito.when(userExternalClient.getUserByLogin(anyString())).thenThrow(new IllegalArgumentException(exceptionMessage));

        UserException userException = assertThrows(UserException.class, () -> userService.getUserByLogin(USER_LOGIN));

        //then
        verify(userExternalClient, times(1)).getUserByLogin(anyString());
        verify(userMapper, never()).mapUserToUserResponse(any(User.class));
        verify(userRequestCounterUserService, never()).callUpdateRequestCounterFunction(anyString());

        assertEquals(userException.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertTrue(userException.getMessage().contains(exceptionMessage));
    }

    private FeignException buildFeignException(HttpStatus httpStatus) {
        Request request = Request.create(Request.HttpMethod.GET, "login/", new HashMap<>(), null, new RequestTemplate());

        return FeignException.errorStatus("getUserByLogin",
                Response.builder()
                        .status(httpStatus.value())
                        .headers(new HashMap<>())
                        .request(request)
                        .reason(httpStatus.getReasonPhrase()).build());
    }
}