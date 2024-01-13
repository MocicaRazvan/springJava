package com.moc.wellness.mapper;

import com.moc.wellness.dto.exercise.ExerciseBody;
import com.moc.wellness.dto.exercise.ExerciseResponse;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Exercise;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ExerciseMapper extends DtoMapper<Exercise, ExerciseBody, ExerciseResponse> {

    @Override
    public void updateModelFromBody(ExerciseBody body, Exercise exercise) {
        exercise.setMuscleGroups(body.getMuscleGroups());
        exercise.setTitle(body.getTitle());
        exercise.setBody(body.getBody());
        exercise.setApproved(false);
    }
}
