package com.moc.wellness.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moc.wellness.controller.UserController;
import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.UserBody;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.enums.Role;
import com.moc.wellness.exception.action.IllegalActionException;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.exception.notFound.PrivateRouteException;
import com.moc.wellness.filter.AuthFilter;
import com.moc.wellness.service.UserService;
import com.moc.wellness.setup.UserSetup;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = UserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests extends UserSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;


    @Test
    @DisplayName("Get user by id success")
    public void getUserByIdSuccess() throws Exception {
        when(userService.getUser(userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(userDto)
                        ));

        verify(userService).getUser(userId);
    }

    @Test
    @DisplayName("Get user not found entity")
    public void getUserNotFoundEntity() throws Exception {
        when(userService.getUser(4L)).thenThrow(new NotFoundEntity("user", 4L));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.id",
                                CoreMatchers.is(4)),
                        MockMvcResultMatchers.jsonPath("$.name",
                                CoreMatchers.is("user")));

        verify(userService).getUser(4L);
    }

    @Test
    @DisplayName("Get roles")
    public void getRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/roles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(
                                        Set.of(Role.USER, Role.TRAINER, Role.ADMIN)
                                )
                        ));
    }

    @Test
    @DisplayName("Get all users success with roles null, empty or all")
    public void getAllUsersSuccess() throws Exception {
        PageableBody pageableBody = PageableBody.builder()
                .size(3)
                .page(0)
                .build();
        PageableResponse<List<UserDto>> pageableResponse
                = PageableResponse.<List<UserDto>>builder()
                .payload(List.of(userDto, trainerDto, adminDto))
                .totalPages(1)
                .totalElements(3)
                .build();
        Set<Role> allRoles = Set.of(Role.USER, Role.TRAINER, Role.ADMIN);
        when(userService.getAllUsers(pageableBody, new HashSet<>())).thenReturn(pageableResponse);
        when(userService.getAllUsers(pageableBody, allRoles)).thenReturn(pageableResponse);

        List<Set<Role>> options = new ArrayList<>();
        options.add(null);
        options.add(new HashSet<>());
        options.add(allRoles);

        for (Set<Role> rolesOption : options) {
            String rolesParam = (rolesOption != null) ? String.join(",", rolesOption.stream().map(Role::name).toList()) : "";
            mockMvc.perform(MockMvcRequestBuilders.patch("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pageableBody))
                            .param("roles", rolesParam))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(pageableResponse)));
        }


        verify(userService, times(2)).getAllUsers(pageableBody, new HashSet<>());
        verify(userService).getAllUsers(pageableBody, allRoles);
    }

    @Test
    @DisplayName("Get all users 1 role (USER)")
    public void getAllUsersUserRole() throws Exception {
        PageableBody pageableBody = PageableBody.builder()
                .size(3)
                .page(0)
                .build();
        PageableResponse<List<UserDto>> pageableResponse
                = PageableResponse.<List<UserDto>>builder()
                .payload(List.of(userDto))
                .totalPages(1)
                .totalElements(1)
                .build();


        when(userService.getAllUsers(pageableBody, Collections.singleton(Role.USER)))
                .thenReturn(pageableResponse);


        mockMvc.perform(MockMvcRequestBuilders.patch("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageableBody))
                        .param("roles", Role.USER.name()))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(pageableResponse)
                        ));


        verify(userService).getAllUsers(pageableBody, Collections.singleton(Role.USER));

    }

    @Test
    @DisplayName("Get all users 2 roles (USER, TRAINER)")
    public void getAllUsersUserTrainerRoles() throws Exception {
        PageableBody pageableBody = PageableBody.builder()
                .size(3)
                .page(0)
                .build();
        PageableResponse<List<UserDto>> pageableResponse
                = PageableResponse.<List<UserDto>>builder()
                .payload(List.of(userDto, trainerDto))
                .totalPages(1)
                .totalElements(1)
                .build();

        Set<Role> roles = Set.of(Role.USER, Role.TRAINER);

        when(userService.getAllUsers(pageableBody, roles))
                .thenReturn(pageableResponse);


        mockMvc.perform(MockMvcRequestBuilders.patch("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageableBody))
                        .param("roles",
                                String.join(",", roles.stream().map(Role::name).toList())
                        ))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(pageableResponse)
                        ));


        verify(userService).getAllUsers(pageableBody, roles);

    }

    @Test
    @DisplayName("Get all users all 3 not valid")
    public void getAllUserAllArgsNotValid() throws Exception {
        PageableBody allNotValid = PageableBody.builder()
                .page(-1)
                .size(0)
                .sortingCriteria(Map.of("ceva", "altceva"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allNotValid)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(3)),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400))
                );

        verify(userService, times(0)).getAllUsers(any(), any());
    }

    @Test
    @DisplayName("Get all users only sorting valid")
    public void getAllUserSortingValid() throws Exception {
        PageableBody allNotValid = PageableBody.builder()
                .page(-1)
                .size(-1)
                .sortingCriteria(Map.of("email", "desc", "firstName", "asc"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allNotValid)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(2)),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400))
                );

        verify(userService, times(0)).getAllUsers(any(), any());
    }

    @Test
    @DisplayName("Make trainer success")
    public void makeTrainerSuccess() throws Exception {
        userDto.setRole(Role.TRAINER);
        when(userService.makeTrainer(userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/admin/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(userDto)
                        ));

        verify(userService).makeTrainer(userId);
    }

    @Test
    @DisplayName("Make trainer already trainer")
    public void makeTrainerAlreadyTrainer() throws Exception {
        when(userService.makeTrainer(trainerId)).thenThrow(new IllegalActionException("User is trainer!"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/admin/" + trainerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.message",
                                CoreMatchers.is("User is trainer!")));

        verify(userService).makeTrainer(trainerId);
    }

    @Test
    @DisplayName("Update user success")
    public void updateUserSuccess() throws Exception {
        when(userService.updateUser(userId, userBody)).then(invocation -> {
            Long id = invocation.getArgument(0);
            UserBody body = invocation.getArgument(1);

            return UserDto.builder()
                    .id(id)
                    .firstName(body.getFirstName())
                    .lastName(body.getLastName())
                    .email(body.getEmail())
                    .build();
        });

        mockMvc.perform(MockMvcRequestBuilders.put("/users/" + userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBody)))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(
                                        UserDto.builder()
                                                .id(userId)
                                                .email(userBody.getEmail())
                                                .firstName(userBody.getFirstName())
                                                .lastName(userBody.getLastName())
                                                .build()
                                )
                        ));

        verify(userService).updateUser(userId, userBody);
    }

    @Test
    @DisplayName("User update private route error")
    public void userUpdatePrivateRoute() throws Exception {
        when(userService.updateUser(userId, userBody)).thenThrow(new PrivateRouteException());

        mockMvc.perform(MockMvcRequestBuilders.put("/users/" + userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBody)))
                .andExpectAll(MockMvcResultMatchers.status().isForbidden(),
                        MockMvcResultMatchers.jsonPath("$.message",
                                CoreMatchers.is("Not allowed!")),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(403)));


        verify(userService).updateUser(userId, userBody);
    }

}
