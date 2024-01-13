package com.moc.wellness.dto.comment;

import com.moc.wellness.dto.common.TitleBody;
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
public class CommentBody extends TitleBody {

}
