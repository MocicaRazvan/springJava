package com.moc.wellness.model;

import com.moc.wellness.model.Templates.Approve;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(name = "Title", columnNames = {"title"})}
)
public class Training extends Approve {

    @Column(nullable = false)
    private double price;

    @ManyToMany
    @JoinTable(name = "training_exercise",
            joinColumns = @JoinColumn(name = "training_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id"))
    private Set<Exercise> exercises;

}
