package com.moc.wellness.utils;

import com.moc.wellness.dto.exercise.ExerciseBodyWithId;
import com.moc.wellness.enums.Role;
import com.moc.wellness.exception.action.NotApprovedEntity;
import com.moc.wellness.exception.action.SubEntityNotOwner;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.model.Exercise;
import com.moc.wellness.model.Templates.Approve;
import com.moc.wellness.model.Templates.IdGenerated;
import com.moc.wellness.model.Templates.ManyToOneUser;
import com.moc.wellness.model.Templates.TitleBody;
import com.moc.wellness.model.Training;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.ExerciseRepository;
import com.moc.wellness.repository.TrainingRepository;
import com.moc.wellness.repository.generic.ApprovedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntitiesUtils {

    private final ExerciseRepository exerciseRepository;
    private final UserUtils userUtils;
    private final TrainingRepository trainingRepository;

    public void setReaction(TitleBody model, UserCustom user, String type) {
        Set<UserCustom> likes = new HashSet<>(model.getUserLikes());
        Set<UserCustom> dislikes = new HashSet<>(model.getUserDislikes());

        if (type.equals("like")) {
            if (likes.contains(user)) {
                likes.remove(user);
            } else {
                likes.add(user);
                dislikes.remove(user);
            }
        } else if (type.equals("dislike")) {
            if (dislikes.contains(user)) {
                dislikes.remove(user);
            } else {
                dislikes.add(user);
                likes.remove(user);
            }
        }

        model.setUserLikes(likes);
        model.setUserDislikes(dislikes);
    }


    public <M extends Approve, R extends ApprovedRepository<M>> Set<M> getActualApproved(
            R repo, Set<Long> ids, String name, boolean pub) {

        return ids.stream().map(i -> {
            M model = repo.findById(i).orElseThrow(() -> new NotFoundEntity(name, i));
            UserCustom authUser = userUtils.getPrincipal();
            if (!pub && !authUser.getRole().equals(Role.ADMIN)) {
                checkSubEntityOwner(model, authUser);
            }
            checkApproved(model, name);
            return model;
        }).collect(Collectors.toSet());
    }

    public Set<Exercise> getActualExercises(Set<Long> exercises) {
        return getActualApproved(exerciseRepository, exercises, "exercise", false);
    }

    public Set<Training> getActualTraining(Set<Long> trainings) {
        return getActualApproved(trainingRepository, trainings, "training", true);
    }

    public void checkSubEntityOwner(ManyToOneUser sub, UserCustom user) {
        if (!sub.getUser().getId().equals(userUtils.getIdByEmail(user.getEmail()))) {
            throw new SubEntityNotOwner(user.getId(), sub.getUser().getId(), sub.getId());
        }
    }

    public void checkApproved(Approve entity, String name) {
        if (!entity.isApproved()) {
            throw new NotApprovedEntity(name, entity.getId());
        }
    }


}
