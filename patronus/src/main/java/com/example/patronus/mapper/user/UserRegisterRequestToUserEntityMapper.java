package com.example.patronus.mapper.user;

import com.example.patronus.mapper.BaseMapper;
import com.example.patronus.models.entity.User;
import com.example.patronus.payload.request.SignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserRegisterRequestToUserEntityMapper extends BaseMapper<SignUpRequest, User> {
    @Named("mapForSaving")
    default User mapForSaving(SignUpRequest userRegisterRequest) {
        return User.builder()
                .email(userRegisterRequest.getEmail())
                .name(userRegisterRequest.getFullName())
                .username(userRegisterRequest.getUsername())
                .build();
    }

    static UserRegisterRequestToUserEntityMapper initialize() {
        return Mappers.getMapper(UserRegisterRequestToUserEntityMapper.class);
    }

}