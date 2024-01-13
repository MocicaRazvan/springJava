package com.moc.wellness.dto.common;

import com.moc.wellness.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "The user dto")
public class UserDto {
    @Schema(description = "The user's id")
    private Long id;

    @Schema(description = "The user's first name")
    private String firstName;

    @Schema(description = "The user's last name")
    private String lastName;

    @Schema(description = "The user's email")
    private String email;

    @Schema(description = "The user's role", defaultValue = "USER")
    private Role role = Role.USER;
}
