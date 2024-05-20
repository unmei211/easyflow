package org.star.taskservice.web.model.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.taskservice.core.entity.task.Task;
import org.star.taskservice.core.entity.workspace.Workspace;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskViaInfo {
    private String id;
    private String name;
    private String description;
    private String workspaceId;
    private String ownerId;

    public TaskViaInfo(final Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.ownerId = task.getOwnerId();
        this.workspaceId = task.getWorkspace().getId();
        this.description = task.getDescription();
    }
}
