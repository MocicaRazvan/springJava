package com.moc.wellness.service.impl;

import com.moc.wellness.dto.training.TrainingBody;
import com.moc.wellness.dto.training.TrainingResponse;
import com.moc.wellness.exception.action.SubEntityUsed;
import com.moc.wellness.mapper.TrainingMapper;
import com.moc.wellness.model.Training;
import com.moc.wellness.repository.TrainingRepository;
import com.moc.wellness.service.TrainingService;
import com.moc.wellness.service.impl.generics.ApprovedServiceImpl;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.EntitiesUtils;
import com.moc.wellness.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrainingImplService
        extends ApprovedServiceImpl<Training, TrainingBody, TrainingResponse, TrainingRepository, TrainingMapper>
        implements TrainingService {

    public TrainingImplService(TrainingRepository modelRepository, TrainingMapper modelMapper, PageableUtilsCustom pageableUtils, UserUtils userUtils, EntitiesUtils entitiesUtils) {
        super(modelRepository, modelMapper, pageableUtils, userUtils, "training", entitiesUtils);
    }

    @Override
    public TrainingResponse deleteModel(Long id) {

        if (modelRepository.countOrdersByTrainingId(id) > 0) {
            throw new SubEntityUsed("training", id);
        }
        return super.deleteModel(id);
    }


}
