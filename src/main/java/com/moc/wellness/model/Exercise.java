package com.moc.wellness.model;

import com.moc.wellness.model.Templates.Approve;
import com.moc.wellness.model.Templates.TitleBody;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(name = "Title", columnNames = {"title"})}
)
public class Exercise extends Approve {
    private List<String> muscleGroups;

}
