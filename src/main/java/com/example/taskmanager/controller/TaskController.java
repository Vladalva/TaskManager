package com.example.taskmanager.controller;

import com.example.taskmanager.dto.CreateTaskDto;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todo")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<TaskDto> getNotCompleted() {
        return taskService.getNotCompleted();
    }

    @PostMapping
    public TaskDto newTask(@RequestBody CreateTaskDto createTaskDto) {
        return taskService.createTask(createTaskDto);

    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}")
    public void markAsCompleted(@PathVariable UUID id) {
        taskService.markAsCompleted(id);
    }
}
