package com.moc.wellness.service;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.UserBody;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.enums.Role;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto getUser(Long id);

    PageableResponse<List<UserDto>> getAllUsers(PageableBody pageableBody, Set<Role> roles);

    UserDto makeTrainer(Long id);

    UserDto updateUser(Long id, UserBody userBody);
}
