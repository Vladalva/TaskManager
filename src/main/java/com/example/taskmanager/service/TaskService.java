package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTaskDto;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskDto createTask(CreateTaskDto createTaskDto) {
        log.debug("Начато создание новой задачи на основе {}", createTaskDto);
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDescription(createTaskDto.getDescription());
        task.setIsCompleted(false);
        task = taskRepository.save(task);
        log.debug("Новая задача создана {}", task);
        return taskMapper.toDto(task);


    }

    @Transactional(readOnly = true)
    public List<TaskDto> getNotCompleted() {
        List<Task> tasks = taskRepository.findAllByIsCompletedFalse();
        log.debug("Получен список не выполненных задач {}", tasks);
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
        log.debug("Задача с идентификатором {} удалена", id);
    }

    @Transactional
    public void markAsCompleted(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найдена задача с id = " + id));
        task.setIsCompleted(true);
        log.debug("Задача с идентификатором {} помечена, как выполненная", id);
    }
}
