package com.moc.wellness.services;

import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.enums.Role;
import com.moc.wellness.exception.action.IllegalActionException;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.exception.notFound.PrivateRouteException;
import com.moc.wellness.mapper.UserMapper;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.UserRepository;
import com.moc.wellness.service.impl.UserServiceImpl;
import com.moc.wellness.setup.UserSetup;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests extends UserSetup {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PageableUtilsCustom pageableUtils;
    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    @DisplayName("Get user by id success")
    public void getUserByIdSuccess() {
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(userUser));
        when(userMapper.fromUserCustomToUserDto(userUser)).thenReturn(userDto);

        UserDto made = userService.getUser(userId);

        Assertions.assertEquals(made, userDto);

        verify(userRepository).findById(userId);
        verify(userMapper).fromUserCustomToUserDto(userUser);
    }


    @Test
    @DisplayName("Get user by id not found")
    public void getUserByIdNotFound() {
        when(userRepository.findById(2L)).thenThrow(
                new NotFoundEntity("user", 2L)
        );

        NotFoundEntity ex =
                Assertions.assertThrows(NotFoundEntity.class,
                        () -> userService.getUser(2L));

        Assertions.assertEquals(ex.getId(), 2L);
        Assertions.assertEquals(ex.getName(), "user");

        verify(userRepository).findById(2L);
        verify(userMapper, times(0)).fromUserCustomToUserDto(any());
    }

    @Test
    @DisplayName("Get all users roles null or empty or all roles")
    public void getAllUsersRolesNull() {
        Set<Role> roles = Set.of(Role.USER, Role.TRAINER, Role.ADMIN);
        Page<UserCustom> mockPage = new PageImpl<>(List.of(userAdmin, userTrainer, userUser),
                PageRequest.of(page, size), 3);
        when(pageableUtils.createSortFromMap(any())).thenReturn(Sort.unsorted());
        when(userRepository.findAllByRoleIn(roles, PageRequest.of(
                pageableBody.getPage(),
                pageableBody.getSize(),
                Sort.unsorted()
        ))).thenReturn(mockPage);
        when(userMapper.fromUserCustomToUserDto(userAdmin)).thenReturn(adminDto);
        when(userMapper.fromUserCustomToUserDto(userTrainer)).thenReturn(trainerDto);
        when(userMapper.fromUserCustomToUserDto(userUser)).thenReturn(userDto);
        List<Set<Role>> options = new ArrayList<>();
        options.add(null);
        options.add(new HashSet<>());
        options.add(roles);

        options.forEach(o -> {

                    PageableResponse<List<UserDto>> resp =
                            userService.getAllUsers(pageableBody, o);

                    Assertions.assertEquals(resp.getTotalElements(), 3);
                    Assertions.assertEquals(resp.getTotalPages(), 1);
                    Assertions.assertEquals(resp.getPayload(), List.of(
                            adminDto, trainerDto, userDto
                    ));
                }
        );
        verify(pageableUtils, times(3)).createSortFromMap(any());
        verify(userRepository, times(3)).findAllByRoleIn(any(), any());
        verify(userMapper, times(9)).fromUserCustomToUserDto(any());
    }

    @Test
    @DisplayName("Get users by a role (USER)")
    public void getUsersByRole() {
        Page<UserCustom> mockPage = new PageImpl<>(List.of(userUser),
                PageRequest.of(page, size), 1);
        when(pageableUtils.createSortFromMap(any())).thenReturn(Sort.unsorted());
        when(userRepository.findAllByRoleIn(Collections.singleton(Role.USER), PageRequest.of(
                pageableBody.getPage(),
                pageableBody.getSize(),
                Sort.unsorted()
        ))).thenReturn(mockPage);
        when(userMapper.fromUserCustomToUserDto(userUser)).thenReturn(userDto);

        PageableResponse<List<UserDto>> resp = userService.getAllUsers(pageableBody, Collections.singleton(Role.USER));

        Assertions.assertEquals(resp.getTotalElements(), 1);
        Assertions.assertEquals(resp.getTotalPages(), 1);
        Assertions.assertEquals(resp.getPayload(), List.of(userDto));

        verify(pageableUtils).createSortFromMap(any());
        verify(userRepository, times(1)).findAllByRoleIn(any(), any());
        verify(userMapper).fromUserCustomToUserDto(userUser);
    }

    @Test
    @DisplayName("Get users by 2 roles (USER, TRAINER)")
    public void getUsersByTwoRoles() {
        Page<UserCustom> mockPage = new PageImpl<>(List.of(userUser, userTrainer),
                PageRequest.of(page, size), 2);
        when(pageableUtils.createSortFromMap(any())).thenReturn(Sort.unsorted());
        when(userRepository.findAllByRoleIn(Set.of(Role.USER, Role.TRAINER), PageRequest.of(
                pageableBody.getPage(),
                pageableBody.getSize(),
                Sort.unsorted()
        ))).thenReturn(mockPage);
        when(userMapper.fromUserCustomToUserDto(userUser)).thenReturn(userDto);
        when(userMapper.fromUserCustomToUserDto(userTrainer)).thenReturn(trainerDto);

        PageableResponse<List<UserDto>> resp = userService.getAllUsers(pageableBody, Set.of(Role.USER, Role.TRAINER));

        Assertions.assertEquals(resp.getTotalElements(), 2);
        Assertions.assertEquals(resp.getTotalPages(), 1);
        Assertions.assertEquals(resp.getPayload(), List.of(userDto, trainerDto));

        verify(pageableUtils).createSortFromMap(any());
        verify(userRepository).findAllByRoleIn(any(), any());
        verify(userMapper, times(2)).fromUserCustomToUserDto(any());
    }

    @Test
    @DisplayName("Make user TRAINER success")
    public void makeUserTrainerSuccess() {
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(userUser));
        when(userRepository.save(userUser)).then(invocation -> {
            UserCustom user = invocation.getArgument(0);
            user.setRole(Role.TRAINER);
            return user;
        });
        when(userMapper.fromUserCustomToUserDto(userUser)).then(invocation -> {
            userDto.setRole(Role.TRAINER);
            return userDto;
        });

        UserDto made = userService.makeTrainer(userId);

        Assertions.assertEquals(made, userDto);


        verify(userRepository).findById(userId);
        verify(userRepository).save(userUser);
        verify(userMapper).fromUserCustomToUserDto(userUser);
    }

    @Test
    @DisplayName("Make user trainer user not found")
    public void makeUserTrainerUserNotFound() {
        when(userRepository.findById(4L)).thenThrow(new NotFoundEntity("user", 4L));

        NotFoundEntity ex =
                Assertions.assertThrows(NotFoundEntity.class, () -> userService.makeTrainer(4L));

        Assertions.assertEquals(ex.getName(), "user");
        Assertions.assertEquals(ex.getId(), 4L);

        verify(userRepository).findById(4L);
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).fromUserCustomToUserDto(any());
    }

    @Test
    @DisplayName("Make user trainer, user already trainer")
    public void makeUserTrainerUserAlreadyTrainer() {
        when(userRepository.findById(trainerId)).thenReturn(Optional.ofNullable(userTrainer));

        IllegalActionException ex =
                Assertions.assertThrows(IllegalActionException.class,
                        () -> userService.makeTrainer(trainerId));

        Assertions.assertEquals(ex.getMessage(), "User is trainer!");

        verify(userRepository).findById(trainerId);
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).fromUserCustomToUserDto(any());
    }

    @Test
    @DisplayName("Make user trainer, user already admin")
    public void makeUserTrainerUserAlreadyAdmin() {
        when(userRepository.findById(adminId)).thenReturn(Optional.ofNullable(userAdmin));

        IllegalActionException ex =
                Assertions.assertThrows(IllegalActionException.class,
                        () -> userService.makeTrainer(adminId));

        Assertions.assertEquals(ex.getMessage(), "User is admin!");

        verify(userRepository).findById(adminId);
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).fromUserCustomToUserDto(any());
    }

    @Test
    @DisplayName("Update user success")
    public void updateUserSuccess() {
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(userUser));
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(userRepository.save(userUser)).then(invocation -> {
            UserCustom user = invocation.getArgument(0);
            user.setEmail(userBody.getEmail());
            user.setFirstName(userBody.getFirstName());
            user.setLastName(userBody.getLastName());
            return user;
        });
        when(userMapper.fromUserCustomToUserDto(userUser)).then(invocation -> {
            UserCustom user = invocation.getArgument(0);
            return UserDto.builder()
                    .lastName(user.getLastName())
                    .firstName(user.getFirstName())
                    .role(user.getRole())
                    .email(user.getEmail())
                    .id(user.getId())
                    .build();
        });

        UserDto expected = UserDto.builder()
                .lastName(userBody.getLastName())
                .firstName(userBody.getFirstName())
                .role(Role.USER)
                .email(userBody.getEmail())
                .id(userId)
                .build();

        UserDto resp = userService.updateUser(userId, userBody);

        Assertions.assertEquals(expected, resp);

        verify(userRepository).findById(userId);
        verify(userUtils).getPrincipal();
        verify(userRepository).save(userUser);
        verify(userMapper).fromUserCustomToUserDto(userUser);
    }

    @Test
    @DisplayName("Update user not found")
    public void updateUserNotFound() {
        when(userRepository.findById(4L)).thenThrow(new NotFoundEntity("user", 4L));

        NotFoundEntity ex =
                Assertions.assertThrows(NotFoundEntity.class,
                        () -> userService.updateUser(4L, userBody));

        Assertions.assertEquals(ex.getId(), 4L);
        Assertions.assertEquals(ex.getName(), "user");


        verify(userRepository).findById(4L);
        verify(userUtils, times(0)).getPrincipal();
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).fromUserCustomToUserDto(any());
    }

    @Test
    @DisplayName("Update user private route")
    public void updateUserPrivateRoute() {
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(userUser));
        when(userUtils.getPrincipal()).thenReturn(userAdmin);

        PrivateRouteException ex =
                Assertions.assertThrows(PrivateRouteException.class,
                        () -> userService.updateUser(userId, userBody));

        Assertions.assertEquals(ex.getMessage(), "Not allowed!");


        verify(userRepository).findById(userId);
        verify(userUtils).getPrincipal();
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).fromUserCustomToUserDto(any());
    }


}
