package com.aga.user.controller;

import com.aga.user.model.UserResponse;
import com.aga.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/{login}", produces = "application/json")
    public UserResponse getUser(@PathVariable @NotBlank String login) {
        return userService.getUserByLogin(login);
    }

}
