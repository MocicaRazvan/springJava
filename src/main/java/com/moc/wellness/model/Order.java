package com.moc.wellness.model;

import com.moc.wellness.model.Templates.ManyToOneUser;
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
@Table(name = "order_custom")
public class Order extends ManyToOneUser {
    private String shippingAddress;
    private boolean payed = false;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_training",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id"))
    private Set<Training> trainings;
}
