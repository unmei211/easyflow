package org.star.taskservice.web.controller.workspace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.star.apigateway.microservice.share.transfer.user.UserCredentials;
import org.star.taskservice.core.entity.workspace.Workspace;
import org.star.taskservice.core.services.workspace.WorkspaceService;
import org.star.taskservice.web.model.workspace.CreateWorkspaceRequest;
import org.star.taskservice.web.model.workspace.WorkspaceViaInfo;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<Workspace> createWorkspace(
            UserCredentials userCredentials,
            @RequestBody CreateWorkspaceRequest createWorkspaceRequest
    ) {
        Workspace workspace = workspaceService
                .createWorkspace(createWorkspaceRequest.getName(), userCredentials.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(workspace);
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceViaInfo> getWorkspaceIfOwner(
            UserCredentials userCredentials,
            @PathVariable String workspaceId
    ) {
        return ResponseEntity.ok(
                new WorkspaceViaInfo(
                        workspaceService
                                .getWorkspaceIfOwner(workspaceId, userCredentials.getUserId())
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceViaInfo>> getUserWorkspaces(
            UserCredentials userCredentials
    ) {
        Set<Workspace> workspaces = workspaceService.getAllWorskpacesByUser(userCredentials.getUserId());
        List<WorkspaceViaInfo> workspaceViaInfos = workspaces.stream().map(WorkspaceViaInfo::new).toList();
        return ResponseEntity.ok(workspaceViaInfos);
    }

    @PostMapping("/delete/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(
            UserCredentials userCredentials,
            @PathVariable(name = "workspaceId") String workspaceId
    ) {
        workspaceService.deleteWorkspace(workspaceId, userCredentials.getUserId());
        return ResponseEntity.ok().build();
    }
}
