package org.star.taskservice.core.entity.workspace;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.taskservice.core.entity.task.Task;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "workspace")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
    private Set<Task> tasks = new HashSet<>();

    public Workspace(String name, String ownerId) {
        this.name = name;
        this.ownerId = ownerId;
    }

    public Workspace addTask(final Task task) {
        getTasks().add(task);
        task.setWorkspace(this);
        return this;
    }
}
