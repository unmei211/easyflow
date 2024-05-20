package org.star.taskservice.core.services.workspace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.apigateway.microservice.share.error.exceptions.core.ConflictException;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.taskservice.core.entity.workspace.Workspace;
import org.star.taskservice.core.repository.task.TaskRepository;
import org.star.taskservice.core.repository.workspace.WorkspaceRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final TaskRepository taskRepository;

    public Workspace createWorkspace(final String workspaceName, final String ownerId) {
        if (workspaceRepository.existsByNameAndOwnerId(workspaceName, ownerId)) {
            log.info("Workspace with provided name [{}] already exist for user [{}]", workspaceName, ownerId);
            throw new ConflictException("Workspace with provided name already exist for user");
        }

        Workspace workspace = new Workspace(workspaceName, ownerId);

        return workspaceRepository.save(workspace);
    }

    public void deleteWorkspace(final String workspaceId, final String ownerId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace not found"));

        if (!workspace.getOwnerId().equals(ownerId)) {
            throw new ForbiddenException("User not owner");
        }

        workspaceRepository.deleteById(workspaceId);
    }

    public Workspace getWorkspaceIfOwner(final String workspaceId, final String ownerId) {
//        taskRepository.find
        return workspaceRepository.findByIdAndOwnerId(workspaceId, ownerId)
                .orElseThrow(() -> new ForbiddenException("You not owner or workspace not exist"));
    }

    public Set<Workspace> getAllWorskpacesByUser(final String userId) {
        return workspaceRepository.findAllByOwnerId(userId);
    }

    public void updateWorkspace(final Workspace workspace) {
        workspaceRepository.save(workspace);
    }

    public Workspace getWorkspaceByNameAndOwner(final String workspaceName, final String ownerId) {
        return workspaceRepository.findByNameAndOwnerId(workspaceName, ownerId).orElseThrow(
                () -> new NotFoundException("Not found workspace" + workspaceName)
        );
    }
}
