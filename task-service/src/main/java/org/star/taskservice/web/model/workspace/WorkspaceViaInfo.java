package org.star.taskservice.web.model.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.taskservice.core.entity.task.Task;
import org.star.taskservice.core.entity.workspace.Workspace;
import org.star.taskservice.web.model.task.TaskViaInfo;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkspaceViaInfo {
    private String ownerId;
    private String workspaceId;
    private String name;
    private List<TaskViaInfo> tasks;

    public WorkspaceViaInfo(final Workspace workspace) {
        this.ownerId = workspace.getOwnerId();
        this.workspaceId = workspace.getId();
        this.name = workspace.getName();
        this.tasks = workspace.getTasks().stream().map(TaskViaInfo::new).toList();
    }
}
