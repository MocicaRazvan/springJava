package com.moc.wellness.dto.post;


import com.moc.wellness.dto.common.Approve;
import com.moc.wellness.dto.common.TitleBody;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.model.user.UserCustom;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "The post response dto")
public class PostResponse extends Approve {
    @Schema(description = "The tags contained in the post.")
    private List<String> tags;
}
