package com.moc.wellness.dto.order;

import com.moc.wellness.dto.training.TrainingResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Schema(description = "The order response dto")
public class OrderResponse extends OrderStructure {

    @Schema(description = "The training's response dtos for the order.")
    private Set<TrainingResponse> trainings;
}
