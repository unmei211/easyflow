package org.star.taskservice.web.model.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTaskRequest {
    private String name;
    private String description;
    private String workspaceId;
}
