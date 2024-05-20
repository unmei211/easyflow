package org.star.taskservice.core.repository.task;


import org.springframework.data.jpa.repository.JpaRepository;
import org.star.taskservice.core.entity.task.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {
//    List<Task> findTasksBy
}
