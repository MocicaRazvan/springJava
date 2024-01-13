package com.moc.wellness.dto.training;

import com.moc.wellness.dto.common.Approve;
import com.moc.wellness.dto.exercise.ExerciseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "The training response dto")
public class TrainingResponse extends Approve {
    @Schema(description = "The price of the training")
    private double price;

    @Schema(description = "The exercise's response dtos for the training.")
    private Set<ExerciseResponse> exercises;
}
