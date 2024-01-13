package com.moc.wellness.services;


import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.dto.training.TrainingResponse;
import com.moc.wellness.exception.action.SubEntityUsed;
import com.moc.wellness.mapper.TrainingMapper;
import com.moc.wellness.model.Training;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.TrainingRepository;
import com.moc.wellness.service.impl.TrainingImplService;
import com.moc.wellness.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;


import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTests {

    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private UserUtils userUtils;

    @Mock
    private TrainingMapper trainingMapper;


    @InjectMocks
    private TrainingImplService trainingService;

    private final Long trainingId = 1L;


    @Test
    @DisplayName("Delete training used")
    public void deleteTrainingUsed() {
        when(trainingRepository.countOrdersByTrainingId(trainingId)).thenReturn(1L);

        SubEntityUsed ex =
                Assertions.assertThrows(SubEntityUsed.class, () -> trainingService.deleteModel(trainingId));

        Assertions.assertEquals(ex.getName(), "training");
        Assertions.assertEquals(ex.getId(), trainingId);

        verify(trainingRepository).countOrdersByTrainingId(trainingId);
    }

    @Test
    @DisplayName("Delete training success")
    public void deleteTrainingSuccess() {
        UserCustom user = UserCustom.builder()
                .id(1L)
                .build();
        Training training = Training.builder()
                .id(trainingId)
                .user(user)
                .build();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .build();
        TrainingResponse trainingResponse = TrainingResponse.builder()
                .id(trainingId)
                .user(userDto)
                .build();
        when(trainingRepository.countOrdersByTrainingId(trainingId)).thenReturn(0L);
        when(userUtils.getPrincipal()).thenReturn(user);
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.ofNullable(training));
        Assertions.assertNotNull(training);
        when(userUtils.hasPermissionToModifyEntity(user, training.getUser().getId()))
                .thenReturn(true);
        doNothing().when(trainingRepository).delete(training);
        when(trainingMapper.fromModelToResponse(training)).thenReturn(trainingResponse);

        TrainingResponse exp = trainingService.deleteModel(trainingId);

        Assertions.assertEquals(exp, trainingResponse);

        verify(trainingRepository).countOrdersByTrainingId(trainingId);
        verify(userUtils).getPrincipal();
        verify(trainingRepository).findById(trainingId);
        verify(userUtils).hasPermissionToModifyEntity(user, training.getUser().getId());
        verify(trainingRepository).delete(training);
        verify(trainingMapper).fromModelToResponse(training);
    }


}
