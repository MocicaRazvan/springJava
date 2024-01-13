package com.moc.wellness.controller;

import com.moc.wellness.controller.generic.ApproveController;
import com.moc.wellness.dto.exercise.ExerciseBody;
import com.moc.wellness.dto.exercise.ExerciseResponse;
import com.moc.wellness.mapper.ExerciseMapper;
import com.moc.wellness.model.Exercise;
import com.moc.wellness.repository.ExerciseRepository;
import com.moc.wellness.service.ExerciseService;
import com.moc.wellness.service.generics.ApprovedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exercises")
@Tag(name = "Exercises Controller")
public class ExerciseController extends ApproveController
        <Exercise, ExerciseBody, ExerciseResponse, ExerciseRepository, ExerciseMapper,
                ApprovedService<Exercise, ExerciseBody, ExerciseResponse, ExerciseRepository, ExerciseMapper>> {
    public ExerciseController(ExerciseService modelService) {
        super(modelService, "exercise");
    }
}
