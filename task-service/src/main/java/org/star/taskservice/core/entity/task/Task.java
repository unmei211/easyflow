package org.star.taskservice.core.entity.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.taskservice.core.entity.workspace.Workspace;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Column
    private String ownerId;

    public Task(
            final String name,
            final String description,
            final Workspace workspace,
            final String ownerId
    ) {
        this.name = name;
        this.description = description;
        this.workspace = workspace;
        this.ownerId = ownerId;
    }

    public Task(
            final String name,
            final String description,
            final String ownerId
    ) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }
}
