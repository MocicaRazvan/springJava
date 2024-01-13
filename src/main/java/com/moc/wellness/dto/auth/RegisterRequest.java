package com.moc.wellness.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "The object provided by the user to register")
public class RegisterRequest extends LoginRequest {
    @NotBlank(message = "The first name should not be empty.")
    @Schema(description = "User's first name")
    private String firstName;
    @NotBlank(message = "The last name should not be empty.")
    @Schema(description = "User's last name")
    private String lastName;
}
