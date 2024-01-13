package com.moc.wellness.dto.exercise;

import com.moc.wellness.dto.common.Approve;
import com.moc.wellness.dto.common.TitleBody;
import com.moc.wellness.dto.common.TitleBodyUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "The exercise response dto")
public class ExerciseResponse extends TitleBodyUser {
    @Schema(description = "The exercise's muscle groups")
    private List<String> muscleGroups;
}
