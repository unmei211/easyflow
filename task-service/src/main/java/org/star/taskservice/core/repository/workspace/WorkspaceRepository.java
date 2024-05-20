package org.star.taskservice.core.repository.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.taskservice.core.entity.workspace.Workspace;

import java.util.Optional;
import java.util.Set;

public interface WorkspaceRepository extends JpaRepository<Workspace, String> {
    boolean existsByNameAndOwnerId(final String name, final String ownerId);

    void deleteById(final String workspaceId);

    Optional<Workspace> findByIdAndOwnerId(final String workspaceId, final String ownerId);

    Set<Workspace> findAllByOwnerId(final String ownerId);

    Optional<Workspace> findByNameAndOwnerId(final String workspaceName, final String ownerId);
}
