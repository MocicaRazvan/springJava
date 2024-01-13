package com.moc.wellness.mapper;

import com.moc.wellness.dto.exercise.ExerciseBodyWithId;
import com.moc.wellness.dto.exercise.ExerciseResponse;
import com.moc.wellness.dto.training.TrainingBody;
import com.moc.wellness.dto.training.TrainingResponse;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Exercise;
import com.moc.wellness.model.Training;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.ExerciseRepository;
import com.moc.wellness.utils.EntitiesUtils;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class TrainingMapper extends DtoMapper<Training, TrainingBody, TrainingResponse> {

    @Autowired
    private EntitiesUtils entitiesUtils;

    @Override
    public void updateModelFromBody(TrainingBody body, Training training) {
        training.setBody(body.getBody());
        training.setTitle(body.getTitle());
        training.setExercises(entitiesUtils.getActualExercises(body.getExercises()));
        training.setPrice(body.getPrice());
        training.setApproved(false);
    }

    @Override
    public Training fromBodyToModel(TrainingBody body) {
        Training training = new Training();
        updateModelFromBody(body, training);
        return training;
    }


}
