package com.moc.wellness.service.impl;

import com.moc.wellness.dto.exercise.ExerciseBody;
import com.moc.wellness.dto.exercise.ExerciseResponse;
import com.moc.wellness.exception.action.SubEntityUsed;
import com.moc.wellness.mapper.ExerciseMapper;
import com.moc.wellness.model.Exercise;
import com.moc.wellness.repository.ExerciseRepository;
import com.moc.wellness.service.ExerciseService;
import com.moc.wellness.service.impl.generics.ApprovedServiceImpl;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.EntitiesUtils;
import com.moc.wellness.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExerciseImplService
        extends ApprovedServiceImpl<Exercise, ExerciseBody, ExerciseResponse, ExerciseRepository, ExerciseMapper>
        implements ExerciseService {


    public ExerciseImplService(ExerciseRepository modelRepository, ExerciseMapper modelMapper, PageableUtilsCustom pageableUtils, UserUtils userUtils, EntitiesUtils entitiesUtils) {
        super(modelRepository, modelMapper, pageableUtils, userUtils, "exercise", entitiesUtils);
    }

    @Override
    public ExerciseResponse deleteModel(Long id) {
        if (modelRepository.countTrainingsByExercisesId(id) > 0) {
            throw new SubEntityUsed("exercise", id);
        }
        return super.deleteModel(id);
    }
}
