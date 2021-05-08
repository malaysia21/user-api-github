package com.aga.user.service;

import com.aga.user.repository.UserRequestCountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRequestCounterUserServiceImpl implements UserRequestCounterUserService {

    private final UserRequestCountRepository userRequestCountRepository;

    @Override
    public void callUpdateRequestCounterFunction(String login) {
        userRequestCountRepository.callUserCounterFunction(login);
    }
}
