package com.moc.wellness.controller;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.UserBody;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.enums.Role;
import com.moc.wellness.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users Controller")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user by id", description = "All authenticated users can access", responses = {
            @ApiResponse(description = "The response will contain an user response dto",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "id", description = "The id of the user",
            required = true, example = "1")})
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Operation(summary = "Get the the possible roles a user can have",
            description = "All authenticated users can access", responses = {
            @ApiResponse(description = "The response will contain a set of all possible user roles",
                    responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getRoles() {
        return ResponseEntity.ok(Set.of(Role.USER, Role.TRAINER, Role.ADMIN));
    }

    @Operation(summary = "Get all the users",
            description = "All authenticated users can access ", responses = {
            @ApiResponse(description = "The response will contain a pageable response with the payload as a list of user response dto",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {
            @Parameter(
                    name = "roles",
                    description = "The roles to filter the users",
                    example = "USER,TRAINER"
            )
    })

    @PatchMapping
    public ResponseEntity<PageableResponse<List<UserDto>>> getAllUsers(
            @Valid @RequestBody PageableBody pageableBody,
            @RequestParam(required = false) Set<Role> roles
    ) {
        return ResponseEntity.ok(userService.getAllUsers(pageableBody, roles));
    }

    @Operation(summary = "Make a user trainer",
            description = "Only admin can access. If the user is already trainer or admin the update will fail.", responses = {
            @ApiResponse(description = "The response will contain a user response dto",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "id", description = "The id of the user",
            required = true, example = "1")})
    @PatchMapping("/admin/{id}")
    public ResponseEntity<UserDto> makeTrainer(@PathVariable Long id) {
        return ResponseEntity.ok(userService.makeTrainer(id));
    }

    @Operation(summary = "Update the user",
            description = """
                    All authenticated users can access
                    For the update to be successfully the user that makes the update should be the same as the one updated
                    """, responses = {
            @ApiResponse(description = "The response will contain a user response dto",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "id", description = "The id of the user",
            required = true, example = "1")})
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserBody userBody) {
        return ResponseEntity.ok(userService.updateUser(id, userBody));
    }


}
