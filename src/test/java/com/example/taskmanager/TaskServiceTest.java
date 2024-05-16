package com.example.taskmanager;

import com.example.taskmanager.dto.CreateTaskDto;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;


    @Test
    public void testCreateTask() {
        CreateTaskDto dto = new CreateTaskDto();
        dto.setDescription("Test Task");

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDescription(dto.getDescription());
        task.setIsCompleted(false);

        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setDescription(task.getDescription());
        taskDto.setIsCompleted(task.getIsCompleted());

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDto);

        TaskDto result = taskService.createTask(dto);

        assertEquals(taskDto, result);
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskMapper, times(1)).toDto(any(Task.class));
    }

    @Test
    public void testGetNotCompleted() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDescription("Test Task");
        task.setIsCompleted(false);

        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setDescription(task.getDescription());
        taskDto.setIsCompleted(task.getIsCompleted());

        when(taskRepository.findAllByIsCompletedFalse()).thenReturn(Collections.singletonList(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        List<TaskDto> result = taskService.getNotCompleted();

        assertEquals(1, result.size());
        assertEquals(taskDto, result.get(0));
        verify(taskRepository, times(1)).findAllByIsCompletedFalse();
        verify(taskMapper, times(1)).toDto(task);
    }

    @Test
    public void testDeleteTask() {
        UUID taskId = UUID.randomUUID();
        doNothing().when(taskRepository).deleteById(taskId);

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    public void testMarkAsCompleted() {
        UUID taskId = UUID.randomUUID();
        Task task = new Task();
        task.setId(taskId);
        task.setDescription("Test Task");
        task.setIsCompleted(false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.markAsCompleted(taskId);

        assertTrue(task.getIsCompleted());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    public void testMarkAsCompletedTaskNotFound() {
        UUID taskId = UUID.randomUUID();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.markAsCompleted(taskId);
        });

        assertEquals("Не найдена задача с id = " + taskId, exception.getMessage());
        verify(taskRepository, times(1)).findById(taskId);
    }
}


