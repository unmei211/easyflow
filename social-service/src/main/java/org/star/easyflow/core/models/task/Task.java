package org.star.easyflow.core.models.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.easyflow.core.models.assigment.Assigment;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(mappedBy = "task", cascade = CascadeType.REMOVE)
    private Assigment assigment;
}
