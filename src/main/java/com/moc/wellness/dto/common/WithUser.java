package com.moc.wellness.dto.common;

import com.moc.wellness.model.user.UserCustom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Schema(description = "The basic dto for an entity that contains a user")
public class WithUser {

    @Schema(description = "The entity's id")
    private Long id;

    @Schema(description = "The entity's user")
    private UserDto user;

}
