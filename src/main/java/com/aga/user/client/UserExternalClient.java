package com.aga.user.client;

import com.aga.user.model.github.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "githubUser", url = "${user.externalClientUrl}")
public interface UserExternalClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/{login}", produces = "application/json")
    User getUserByLogin(@PathVariable("login") String login);
}


