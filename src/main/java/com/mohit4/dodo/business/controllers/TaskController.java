package com.mohit4.dodo.business.controllers;

import com.mohit4.dodo.persistence.contants.TaskConstants;
import com.mohit4.dodo.persistence.models.Task;
import com.mohit4.dodo.persistence.repositories.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dodo/task")
public class TaskController {

    Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        for ( Task task : taskRepository.findAll() ) {
            taskList.add( task );
        }
        logger.debug("Fetched total {} task(s) : {}", taskList.size(), taskList);
        return taskList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable(value="id") long id) {
        Optional<Task> task = taskRepository.findById(id);
        if ( task.isPresent() ) {
            logger.debug("Found task by id : {} -> {}", id, task);
            return ResponseEntity.ok().body(task.get());
        } else {
            logger.debug("No task with id : {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Task saveTask(@Validated @RequestBody Task task) {
        task.createTask();
        logger.debug("Saving task : {}", task);
        return taskRepository.save(task);
    }

    @PostMapping("/retrieveTaskByStatus")
    public ResponseEntity<List<Task>> retrieveTaskByStatus(@Validated @RequestBody Task task) {
        List<Task> taskList = new ArrayList<>();
        for ( Task oneTask : taskRepository.findAll() ) {
            if ( task.getStatus().equalsIgnoreCase(oneTask.getStatus()) )
                taskList.add( task );
        }
        logger.debug("Found total {} task(s) with status = {} : {}", taskList.size(), task.getStatus(), taskList);
        return ResponseEntity.ok().body(taskList);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Task> moveTask(@Validated @RequestBody Task task, @PathVariable(value = "id") long id) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if ( existingTask.isPresent() ) {
            existingTask.get().setStatus(task.getStatus());
            existingTask.get().setLastModifiedDate(OffsetDateTime.now());
            if ( existingTask.get().getStatus().equalsIgnoreCase(TaskConstants.TASK_STATUS_COMPLETED) )
                existingTask.get().completeTask();
            else if ( existingTask.get().getStatus().equalsIgnoreCase(TaskConstants.TASK_STATUS_INPROGRESS) )
                existingTask.get().startTask();
            taskRepository.save(existingTask.get());
            logger.debug("Updated task with id : {} -> {}", id, task);
            return ResponseEntity.ok().body(existingTask.get());
        } else {
            logger.error("No task found with id : {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Task> deleteTask(@PathVariable(value = "id") Long id) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if ( existingTask.isPresent() ) {
            logger.debug("Found task with id : {}, Deleting -> {}", id, existingTask.get());
            taskRepository.deleteById(id);
            return ResponseEntity.ok().body(existingTask.get());
        } else {
            logger.error("No task found with id : {}", id);
            return ResponseEntity.notFound().build();
        }
    }

}
