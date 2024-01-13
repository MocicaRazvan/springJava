package com.moc.wellness.repository;

import com.moc.wellness.model.Exercise;
import com.moc.wellness.repository.generic.ApprovedRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExerciseRepository extends ApprovedRepository<Exercise> {

    @Query("""
                select count(t) from Training t join t.exercises e where e.id=:exerciseId
                       
            """)
    Long countTrainingsByExercisesId(Long exerciseId);

}
