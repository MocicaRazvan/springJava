package com.moc.wellness.service;

import com.moc.wellness.dto.exercise.ExerciseBody;
import com.moc.wellness.dto.exercise.ExerciseResponse;
import com.moc.wellness.mapper.ExerciseMapper;
import com.moc.wellness.model.Exercise;
import com.moc.wellness.repository.ExerciseRepository;
import com.moc.wellness.service.generics.ApprovedService;

public interface ExerciseService extends ApprovedService
        <Exercise, ExerciseBody, ExerciseResponse, ExerciseRepository, ExerciseMapper> {
}
