package com.aga.user.service;

import com.aga.user.model.UserResponse;

public interface UserService {

    UserResponse getUserByLogin(String login);
}
