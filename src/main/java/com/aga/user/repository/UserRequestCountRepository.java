package com.aga.user.repository;

import javax.transaction.Transactional;

@Transactional
public interface UserRequestCountRepository {

    void callUserCounterFunction(String login);
}
