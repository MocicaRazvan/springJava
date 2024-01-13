package com.moc.wellness.controller;

import com.moc.wellness.controller.generic.ApproveController;
import com.moc.wellness.dto.training.TrainingBody;
import com.moc.wellness.dto.training.TrainingResponse;
import com.moc.wellness.mapper.TrainingMapper;
import com.moc.wellness.model.Training;
import com.moc.wellness.repository.TrainingRepository;
import com.moc.wellness.service.TrainingService;
import com.moc.wellness.service.generics.ApprovedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainings")
@Tag(name = "Trainings Controller")
public class TrainingController extends ApproveController
        <Training, TrainingBody, TrainingResponse, TrainingRepository, TrainingMapper,
                ApprovedService<Training, TrainingBody, TrainingResponse, TrainingRepository, TrainingMapper>> {
    public TrainingController(TrainingService modelService) {
        super(modelService, "training");
    }
}
