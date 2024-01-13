package com.moc.wellness.utils;

import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.enums.Role;
import com.moc.wellness.exception.action.NotApprovedEntity;
import com.moc.wellness.exception.action.SubEntityNotOwner;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.model.Exercise;
import com.moc.wellness.model.Post;
import com.moc.wellness.model.Training;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.ExerciseRepository;
import com.moc.wellness.repository.TrainingRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntitiesUtilsTests {
    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private EntitiesUtils entitiesUtils;

    private final Long adminId = 1L;
    private final Long trainerId = 2L;
    private final Long userId = 3L;
    private final Long exerciseId = 1L;
    private final Long trainingId = 1L;
    private UserCustom userTrainer;
    private UserCustom userAdmin;
    private UserCustom userUser;
    private UserDto trainerDto;
    private UserDto adminDto;

    private Training training;

    private Exercise exercise;


    @BeforeEach
    public void setUp() {
        userTrainer = UserCustom.builder()
                .id(trainerId)
                .email("marcel@gmail.com")
                .firstName("Marcel")
                .lastName("Popescu")
                .password("1234")
                .role(Role.TRAINER)
                .build();
        userAdmin = UserCustom.builder()
                .id(adminId)
                .email("Admin@gmail.com")
                .firstName("Razvan")
                .lastName("Mocica")
                .password("1234")
                .role(Role.ADMIN)
                .build();

        trainerDto = UserDto.builder()
                .id(trainerId)
                .email("marcel@gmail.com")
                .firstName("Marcel")
                .lastName("Popescu")
                .role(Role.TRAINER)
                .build();
        adminDto = UserDto.builder()
                .id(adminId)
                .email("Admin@gmail.com")
                .firstName("Razvan")
                .lastName("Mocica")
                .role(Role.ADMIN)
                .build();
        exercise = Exercise.builder()
                .id(exerciseId)
                .muscleGroups(List.of("bicep"))
                .title("test")
                .body("test")
                .userLikes(Set.of(userAdmin, userTrainer))
                .userDislikes(new HashSet<>())
                .user(userTrainer)
                .approved(true)
                .build();

        training = Training.builder()
                .id(trainerId)
                .user(userTrainer)
                .title("test")
                .body("test")
                .userLikes(new HashSet<>())
                .userDislikes(Set.of(userAdmin))
                .exercises(Set.of(exercise))
                .price(10)
                .approved(true)
                .build();
        userUser = UserCustom.builder()
                .id(userId)
                .email("user@gmail.com")
                .firstName("User")
                .lastName("user")
                .password("1234")
                .role(Role.USER)
                .build();

    }

    @Test
    @DisplayName("""
            Set dislike to exercise.
            The likes should be userTrainer.
            The dislikes should be userAdmin.
            """)
    public void adminDislikeExercise() {
        entitiesUtils.setReaction(exercise, userAdmin, "dislike");

        Assertions.assertEquals(exercise.getUserLikes().size(), 1);
        Assertions.assertEquals(exercise.getUserDislikes().size(), 1);
        Assertions.assertEquals(exercise.getUserLikes(), Set.of(userTrainer));
        Assertions.assertEquals(exercise.getUserDislikes(), Set.of(userAdmin));

    }


    @Test
    @DisplayName("""
            Set admin like to training
            The likes should be userAdmin
            The dislikes should be empty
            """)
    public void adminLikesTraining() {
        entitiesUtils.setReaction(training, userAdmin, "like");

        Assertions.assertEquals(training.getUserLikes().size(), 1);
        Assertions.assertEquals(training.getUserDislikes().size(), 0);
        Assertions.assertEquals(training.getUserLikes(), Set.of(userAdmin));
        Assertions.assertEquals(training.getUserDislikes(), Collections.emptySet());
    }

    @Test
    @DisplayName("""
            Set admin dislike to training
            The likes should be empty
            The dislikes should be empty
            """)
    public void adminDislikesTraining() {
        entitiesUtils.setReaction(training, userAdmin, "dislike");

        Assertions.assertEquals(training.getUserLikes().size(), 0);
        Assertions.assertEquals(training.getUserDislikes().size(), 0);
        Assertions.assertEquals(training.getUserLikes(), Collections.emptySet());
        Assertions.assertEquals(training.getUserDislikes(), Collections.emptySet());
    }

    @Test
    @DisplayName("""
            Set like to exercise.
            The likes should be userTrainer.
            The dislikes should be empty.
            """)
    public void adminLikeExercise() {
        entitiesUtils.setReaction(exercise, userAdmin, "like");

        Assertions.assertEquals(exercise.getUserLikes().size(), 1);
        Assertions.assertEquals(exercise.getUserDislikes().size(), 0);
        Assertions.assertEquals(exercise.getUserLikes(), Set.of(userTrainer));
        Assertions.assertEquals(exercise.getUserDislikes(), Collections.emptySet());

    }

    @Test
    @DisplayName("Check sub entity owner success")
    public void checkSubEntityOwnerSuccess() {
        when(userUtils.getIdByEmail(userTrainer.getEmail())).thenReturn(trainerId);

        entitiesUtils.checkSubEntityOwner(exercise, userTrainer);

        verify(userUtils).getIdByEmail(userTrainer.getEmail());
    }

    @Test
    @DisplayName("Check sub entity owner fail")
    public void checkSubEntityOwnerSuccessAdmin() {
        when(userUtils.getIdByEmail(userUser.getEmail())).thenReturn(userId);

        SubEntityNotOwner ex = Assertions.assertThrows(
                SubEntityNotOwner.class,
                () -> entitiesUtils.checkSubEntityOwner(exercise, userUser)
        );


        Assertions.assertEquals(ex.getEntityId(), exercise.getId());
        Assertions.assertEquals(ex.getEntityUserId(), exercise.getUser().getId());
        Assertions.assertEquals(ex.getAuthId(), userUser.getId());

        verify(userUtils).getIdByEmail(userUser.getEmail());
    }

    @Test
    @DisplayName("Check approved true")
    public void checkApprovedTrue() {
        entitiesUtils.checkApproved(exercise, "exercise");
    }

    @Test
    @DisplayName("Check approved not approved")
    public void checkApprovedNotApproved() {
        exercise.setApproved(false);

        NotApprovedEntity ex =
                Assertions.assertThrows(NotApprovedEntity.class,
                        () -> entitiesUtils.checkApproved(exercise, "exercise"));

        Assertions.assertEquals(ex.getName(), "exercise");
        Assertions.assertEquals(ex.getId(), exercise.getId());

    }

    @Test
    @DisplayName("Get actual exercise owner success")
    public void getActualExerciseOwnerSuccess() {
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        when(userUtils.getIdByEmail(userTrainer.getEmail())).thenReturn(userTrainer.getId());
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.ofNullable(exercise));

        Set<Exercise> exercises = entitiesUtils.getActualExercises(Set.of(exerciseId));

        Assertions.assertEquals(exercises.size(), 1);
        Assertions.assertEquals(
                exercises.stream().filter(ex -> ex.equals(exercise))
                        .toList().size(), 1);

        verify(userUtils).getPrincipal();
        verify(exerciseRepository).findById(exerciseId);
        verify(userUtils).getIdByEmail(userTrainer.getEmail());
    }

    @Test
    @DisplayName("Get actual exercise admin success")
    public void getActualExerciseAdminSuccess() {
        when(userUtils.getPrincipal()).thenReturn(userAdmin);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.ofNullable(exercise));

        Set<Exercise> exercises = entitiesUtils.getActualExercises(Set.of(exerciseId));

        Assertions.assertEquals(exercises.size(), 1);
        Assertions.assertEquals(
                exercises.stream().filter(ex -> ex.equals(exercise))
                        .toList().size(), 1);

        verify(userUtils).getPrincipal();
        verify(exerciseRepository).findById(exerciseId);
        verify(userUtils, times(0)).getIdByEmail(any());
    }

    @Test
    @DisplayName("Get actual exercise entity not found")
    public void getActualExerciseEntityNotFound() {
        when(exerciseRepository.findById(2L)).
                thenThrow(new NotFoundEntity("exercise", 2L));


        NotFoundEntity ex =
                Assertions.assertThrows(NotFoundEntity.class,
                        () -> entitiesUtils.getActualExercises(Set.of(2L)));

        Assertions.assertEquals(ex.getName(), "exercise");
        Assertions.assertEquals(ex.getId(), 2L);

        verify(userUtils, times(0)).getPrincipal();
        verify(exerciseRepository).findById(2L);
        verify(userUtils, times(0)).getIdByEmail(any());
    }

    @Test
    @DisplayName("Get actual exercise not approved")
    public void getActualExerciseNotApproved() {
        exercise.setApproved(false);
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        when(userUtils.getIdByEmail(userTrainer.getEmail())).thenReturn(userTrainer.getId());
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.ofNullable(exercise));


        NotApprovedEntity ex =
                Assertions.assertThrows(NotApprovedEntity.class,
                        () -> entitiesUtils.getActualExercises(Set.of(exerciseId)));


        Assertions.assertEquals(ex.getName(), "exercise");
        Assertions.assertEquals(ex.getId(), exerciseId);

        verify(userUtils).getPrincipal();
        verify(exerciseRepository).findById(exerciseId);
        verify(userUtils).getIdByEmail(userTrainer.getEmail());
    }

    @Test
    @DisplayName("Exercise sub entity owner ")
    public void getActualExerciseSubEntityError() {
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.ofNullable(exercise));
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(userUtils.getIdByEmail(userUser.getEmail())).thenReturn(userId);

        SubEntityNotOwner ex =
                Assertions.assertThrows(SubEntityNotOwner.class,
                        () -> entitiesUtils.getActualExercises(Set.of(exerciseId)));

        Assertions.assertEquals(ex.getEntityId(), exerciseId);
        Assertions.assertEquals(ex.getEntityUserId(), trainerId);
        Assertions.assertEquals(ex.getAuthId(), userId);


        verify(exerciseRepository).findById(exerciseId);
        verify(userUtils).getPrincipal();
        verify(userUtils).getIdByEmail(userUser.getEmail());
    }

    @Test
    @DisplayName("Training user success")
    public void getActualTrainingsUserSuccess() {
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.ofNullable(training));
        when(userUtils.getPrincipal()).thenReturn(userUser);

        Set<Training> trainings = entitiesUtils.getActualTraining(Set.of(trainingId));

        Assertions.assertEquals(trainings.size(), 1);
        Assertions.assertEquals(trainings.stream().filter(t -> t.equals(training)).toList().size(), 1);

        verify(trainingRepository).findById(trainingId);
        verify(userUtils).getPrincipal();
        verify(userUtils, times(0)).getIdByEmail(userUser.getEmail());
    }

}
