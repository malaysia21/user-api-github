package com.aga.user.controller;

import com.aga.user.exception.UserException;
import com.aga.user.exception.UserExceptionEnum;
import com.aga.user.model.UserResponse;
import com.aga.user.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final String GET_USER_URL = "/users/{login}";

    private static final String LOGIN = "login";
    private static final String UNKNOWN_LOGIN = "unknownLogin";

    private static final String USER_NAME = "name";
    private static final long USER_ID = 100;
    private static final String NOT_FOUND = "NOT_FOUND";



    @Test
    void getUserReturn200() throws Exception {
        UserResponse userResponse = UserResponse.builder().id(USER_ID).name(USER_NAME).build();

        given(userService.getUserByLogin(anyString())).willReturn(userResponse);

        mockMvc.perform(get(GET_USER_URL, LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo((int) USER_ID)))
                .andExpect(jsonPath("$.name", equalTo(USER_NAME)));

        verify(userService, times(1)).getUserByLogin(anyString());

    }

    @Test
    void getUserReturn404() throws Exception {
        UserException exception = UserException.UserExceptionFactory.getException(UserExceptionEnum.USER_NOT_FOUND_EXCEPTION, UNKNOWN_LOGIN);

        given(userService.getUserByLogin(anyString())).willThrow(exception);

        mockMvc.perform(get(GET_USER_URL, UNKNOWN_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.httpStatus", equalTo(NOT_FOUND)))
                .andExpect(jsonPath("$.message", Matchers.containsString(UNKNOWN_LOGIN)));

        verify(userService, times(1)).getUserByLogin(anyString());

    }
}