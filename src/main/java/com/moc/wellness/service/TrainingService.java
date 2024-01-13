package com.moc.wellness.service;

import com.moc.wellness.dto.training.TrainingBody;
import com.moc.wellness.dto.training.TrainingResponse;
import com.moc.wellness.mapper.TrainingMapper;
import com.moc.wellness.model.Training;
import com.moc.wellness.repository.TrainingRepository;
import com.moc.wellness.service.generics.ApprovedService;

public interface TrainingService extends ApprovedService<Training, TrainingBody, TrainingResponse, TrainingRepository, TrainingMapper> {
}
