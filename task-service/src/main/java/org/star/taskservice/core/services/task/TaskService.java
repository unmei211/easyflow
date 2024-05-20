package org.star.taskservice.core.services.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.taskservice.core.entity.task.Task;
import org.star.taskservice.core.entity.workspace.Workspace;
import org.star.taskservice.core.repository.task.TaskRepository;
import org.star.taskservice.core.services.workspace.WorkspaceService;
import org.star.taskservice.web.model.task.CreateTaskRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final WorkspaceService workspaceService;

    public Task createTask(
            final String userId,
            final String taskName,
            final String description,
            final String workspaceId
    ) {
        Workspace workspace = workspaceService.getWorkspaceIfOwner(workspaceId, userId);
        Task task = taskRepository.save(new Task(taskName, description, workspace, userId));
        workspace.addTask(task);
        workspaceService.updateWorkspace(workspace);
        return task;
    }

    public void delegateTask(String from, String to, CreateTaskRequest task, String workspaceName) {
        Workspace workspace = workspaceService.getWorkspaceByNameAndOwner(workspaceName, to);
        taskRepository.save(new Task(
                task.getName(),
                task.getDescription(),
                workspace,
                from
        ));
    }
}
