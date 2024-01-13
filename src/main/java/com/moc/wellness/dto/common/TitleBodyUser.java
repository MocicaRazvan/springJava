package com.moc.wellness.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Schema(description = "The basic dto for an entity that contains title, body and a user")
public class TitleBodyUser extends WithUser {
    @NotBlank(message = "The body should not be blank.")
    @Schema(description = "The entity's body")
    private String body;

    @NotBlank(message = "The title should not be blank.")
    @Schema(description = "The entity's title")
    private String title;

    @Schema(description = "The user that liked the entity")
    private Set<UserDto> userDislikes = new HashSet<>();
    
    @Schema(description = "The user that disliked the entity")
    private Set<UserDto> userLikes = new HashSet<>();
}
