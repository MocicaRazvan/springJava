package com.moc.wellness.repository;

import com.moc.wellness.model.Training;
import com.moc.wellness.repository.generic.ApprovedRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrainingRepository extends ApprovedRepository<Training> {
    @Query("""
                    select count(o) from  Order o join o.trainings t where
                    t.id=:trainingId
            """)
    Long countOrdersByTrainingId(Long trainingId);
}
