package com.moc.wellness.dto.comment;

import com.moc.wellness.dto.common.TitleBodyUser;
import com.moc.wellness.dto.post.PostResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
//@AllArgsConstructor
@Data
@SuperBuilder
@Schema(description = "The comment response dto")
public class CommentResponse extends TitleBodyUser {

    @Schema(description = "The post response dto for which the comment belongs")
    private PostResponse post;
}
