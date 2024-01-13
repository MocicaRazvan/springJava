package com.moc.wellness.model;

import com.moc.wellness.model.Templates.Approve;
import jakarta.persistence.*;
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
public class Post extends Approve {

    private List<String> tags;

   
}
