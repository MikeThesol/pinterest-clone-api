package com.thesol.pinterest_clone.mapper;

import com.thesol.pinterest_clone.models.User;
import com.thesol.pinterest_clone.models.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> users);
}
