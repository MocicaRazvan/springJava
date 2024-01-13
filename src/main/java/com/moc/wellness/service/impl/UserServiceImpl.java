package com.moc.wellness.service.impl;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.UserBody;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.exception.action.IllegalActionException;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.exception.notFound.PrivateRouteException;
import com.moc.wellness.mapper.UserMapper;
import com.moc.wellness.enums.Role;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.UserRepository;
import com.moc.wellness.service.UserService;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PageableUtilsCustom pageableUtils;
    private final UserUtils userUtils;

    @Override
    public UserDto getUser(Long id) {
        return userMapper.fromUserCustomToUserDto(getUserById(id));
    }

    @Override
    public PageableResponse<List<UserDto>> getAllUsers(PageableBody pageableBody, Set<Role> roles) {

        if (roles == null) {
            roles = new HashSet<>();
        }

        if (roles.isEmpty()) {
            roles.add(Role.USER);
            roles.add(Role.TRAINER);
            roles.add(Role.ADMIN);
        }

        Page<UserCustom> pageUser = userRepository.findAllByRoleIn(roles, PageRequest.of(
                pageableBody.getPage(),
                pageableBody.getSize(),
                pageableUtils.createSortFromMap(pageableBody.getSortingCriteria())
        ));

        return PageableResponse.<List<UserDto>>builder()
                .totalElements(pageUser.getTotalElements())
                .totalPages(pageUser.getTotalPages())
                .payload(pageUser.getContent()
                        .stream().map(userMapper::fromUserCustomToUserDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public UserDto makeTrainer(Long id) {
        UserCustom user = getUserById(id);
        if (user.getRole().equals(Role.ADMIN)) {
            throw new IllegalActionException("User is admin!");
        } else if (user.getRole().equals(Role.TRAINER)) {
            throw new IllegalActionException("User is trainer!");
        }
        user.setRole(Role.TRAINER);
        UserCustom savedUser = userRepository.save(user);
        return userMapper.fromUserCustomToUserDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserBody userBody) {
        UserCustom user = getUserById(id);
        UserCustom authUser = userUtils.getPrincipal();

        if (!user.getId().equals(authUser.getId())) {
            throw new PrivateRouteException();
        }

        user.setEmail(userBody.getEmail());
        user.setLastName(userBody.getLastName());
        user.setFirstName(userBody.getFirstName());

        UserCustom savedUser = userRepository.save(user);

        return userMapper.fromUserCustomToUserDto(savedUser);
    }

    private UserCustom getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundEntity("user", id));
    }
}
