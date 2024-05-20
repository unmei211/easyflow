package org.star.taskservice.core.entity.taskhistory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String task_id;

    @Column(nullable = false)
    private Long whenResolved;

    @Column(nullable = false)
    private Boolean complete;
}
