package com.moc.wellness.model.Templates;

import com.moc.wellness.model.user.UserCustom;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class TitleBody extends ManyToOneUser {

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "userLike_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserCustom> userLikes = new HashSet<>();
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "userDislike_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserCustom> userDislikes = new HashSet<>();
}
