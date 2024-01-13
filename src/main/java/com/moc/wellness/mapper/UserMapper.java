package com.moc.wellness.mapper;

import com.moc.wellness.dto.auth.AuthResponse;
import com.moc.wellness.dto.auth.LoginRequest;
import com.moc.wellness.dto.auth.RegisterRequest;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.model.user.UserCustom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(registerRequest.getPassword()))")
    @Mapping(target = "role", expression = "java(com.moc.wellness.enums.Role.USER)")
    public abstract UserCustom fromRegisterRequestToUserCustom(RegisterRequest registerRequest);

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(loginRequest.getPassword()))")
    public abstract UserCustom fromLoginRequestToUserCustom(LoginRequest loginRequest);

    public abstract AuthResponse fromUserCustomToAuthResponse(UserCustom userCustom);

    public abstract UserDto fromUserCustomToUserDto(UserCustom userCustom);

    public abstract UserCustom fromUserDtoToUserCustom(UserDto userDto);

}
