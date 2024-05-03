package org.star.easyflow.core.models.assigment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.easyflow.core.models.contract.Contract;
import org.star.easyflow.core.models.task.Task;

@Entity
@Table(name = "assigment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"contract_id", "task_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Assigment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @OneToOne
    @JoinColumn(name = "task_id", unique = true)
    private Task task;
}
