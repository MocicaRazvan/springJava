package com.moc.wellness.services;

import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.dto.exercise.ExerciseResponse;
import com.moc.wellness.exception.action.SubEntityUsed;
import com.moc.wellness.mapper.ExerciseMapper;
import com.moc.wellness.model.Exercise;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.ExerciseRepository;
import com.moc.wellness.service.impl.ExerciseImplService;
import com.moc.wellness.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTests {
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private UserUtils userUtils;

    @Mock
    private ExerciseMapper exerciseMapper;
    @InjectMocks
    private ExerciseImplService exerciseService;

    private final Long exerciseId = 1L;


    @Test
    @DisplayName("Delete exercise used")
    public void deleteExerciseUsed() {
        when(exerciseRepository.countTrainingsByExercisesId(exerciseId)).thenReturn(1L);

        SubEntityUsed ex = Assertions.assertThrows(SubEntityUsed.class,
                () -> exerciseService.deleteModel(exerciseId));
        Assertions.assertEquals(ex.getName(), "exercise");
        Assertions.assertEquals(ex.getId(), exerciseId);

        verify(exerciseRepository).countTrainingsByExercisesId(exerciseId);
    }

    @Test
    @DisplayName("Delete exercise success")
    public void deleteExerciseSuccess() {
        UserCustom user = UserCustom.builder()
                .id(1L)
                .build();
        Exercise exercise = Exercise.builder()
                .id(exerciseId)
                .user(user)
                .build();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .build();
        ExerciseResponse exerciseResponse = ExerciseResponse.builder()
                .id(exerciseId)
                .user(userDto)
                .build();
        when(exerciseRepository.countTrainingsByExercisesId(exerciseId))
                .thenReturn(0L);
        when(userUtils.getPrincipal()).thenReturn(user);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.ofNullable(exercise));
        Assertions.assertNotNull(exercise);
        when(userUtils.hasPermissionToModifyEntity(user, exercise.getUser().getId()))
                .thenReturn(true);
        doNothing().when(exerciseRepository).delete(exercise);
        when(exerciseMapper.fromModelToResponse(exercise)).thenReturn(exerciseResponse);

        ExerciseResponse exp = exerciseService.deleteModel(exerciseId);

        Assertions.assertEquals(exp, exerciseResponse);

        verify(exerciseRepository).countTrainingsByExercisesId(exerciseId);
        verify(userUtils).getPrincipal();
        verify(exerciseRepository).findById(exerciseId);
        verify(userUtils).hasPermissionToModifyEntity(user, exercise.getUser().getId());
        verify(exerciseRepository).delete(exercise);
        verify(exerciseMapper).fromModelToResponse(exercise);
    }

}
