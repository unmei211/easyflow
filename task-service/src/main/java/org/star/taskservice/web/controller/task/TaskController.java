package org.star.taskservice.web.controller.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.star.apigateway.microservice.service.social.client.feignclient.SocialServiceFeignClient;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.share.transfer.user.UserCredentials;
import org.star.taskservice.core.services.task.TaskService;
import org.star.taskservice.web.model.task.CreateTaskRequest;
import org.star.taskservice.web.model.task.TaskViaInfo;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final SocialServiceFeignClient socialServiceFeignClient;

    @PostMapping
    public ResponseEntity<TaskViaInfo> createTask(
            UserCredentials userCredentials,
            @RequestBody CreateTaskRequest createTaskRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TaskViaInfo(taskService.createTask(
                        userCredentials.getUserId(),
                        createTaskRequest.getName(),
                        createTaskRequest.getDescription(),
                        createTaskRequest.getWorkspaceId())));
    }

    @PostMapping("/send-to/{userId}/in/{workspaceName}")
    public ResponseEntity<?> delegateTask(
            UserCredentials userCredentials,
            @RequestBody CreateTaskRequest createTaskRequest,
            @PathVariable(name = "userId") String to,
            @PathVariable(name = "workspaceName") String workspaceName
    ) {
        if (!socialServiceFeignClient.userIsFriend(userCredentials.getUserId(), to)) {
            throw new ForbiddenException("You not friend");
        }
        taskService.delegateTask(userCredentials.getUserId(), to, createTaskRequest, workspaceName);
        return ResponseEntity.ok().build();
    }
}
