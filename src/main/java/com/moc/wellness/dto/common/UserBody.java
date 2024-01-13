package com.moc.wellness.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "The user request object")
public class UserBody {

    @NotBlank(message = "The first name should not be empty.")
    @Schema(description = "User's first name")
    private String firstName;

    @NotBlank(message = "The last name should not be empty.")
    @Schema(description = "User's last name")
    private String lastName;

    @NotBlank(message = "The email should not be empty.")
    @Email(message = "The email should be valid!")
    @Schema(description = "User's email")
    private String email;
}
