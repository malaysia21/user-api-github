package com.aga.user.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {

    private long id;
    private String login;
    private String name;
    private String type;
    private String avatarUrl;
    private ZonedDateTime createdAt;
    private long calculations;

}
