package com.aga.user.mapper;


import com.aga.user.model.UserResponse;
import com.aga.user.model.github.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "user", target = "calculations", qualifiedBy = UserCalculations.class)
    @Mapping(source = "user.avatar_url", target = "avatarUrl")
    @Mapping(source = "user.created_at", target = "createdAt")
    UserResponse mapUserToUserResponse(User user);


    @UserCalculations
    static long userCalculations(User user) {
        if (user.getFollowers() == 0) {
            return 0;
        }
        return 6 / user.getFollowers() * (2 + user.getPublic_repos());
    }
}
