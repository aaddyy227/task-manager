package com.taskmanager.service;

import com.taskmanager.dto.TaskDTO;
import com.taskmanager.dto.TaskHistoryDTO;
import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskUpdateRequest;
import com.taskmanager.exception.DuplicateTaskException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskHistory;
import com.taskmanager.repository.TaskHistoryRepository;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskHistoryRepository taskHistoryRepository;
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Captor
    private ArgumentCaptor<Task> taskCaptor;

    private Task task;
    private TaskDTO taskDTO;
    private TaskRequest taskRequest;
    private TaskUpdateRequest taskUpdateRequest;
    @Captor
    private ArgumentCaptor<TaskHistory> taskHistoryCaptor;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId("1");
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.of(2024, 4, 7));
        task.setResponsible("John Doe");

        taskDTO = new TaskDTO();
        taskDTO.setId("1");
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setDueDate(LocalDate.of(2024, 4, 7));
        taskDTO.setResponsible("John Doe");

        taskRequest = new TaskRequest();
        taskRequest.setTitle("Test Task");
        taskRequest.setDescription("Test Description");
        taskRequest.setDueDate(LocalDate.of(2024, 4, 7));
        taskRequest.setResponsible("John Doe");

        taskUpdateRequest = new TaskUpdateRequest();
        taskUpdateRequest.setTitle("Updated Task");
        taskUpdateRequest.setDescription("Updated Description");
        taskUpdateRequest.setDueDate(LocalDate.of(2024, 4, 7));
        taskUpdateRequest.setResponsible("Jane Doe");
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById("1")).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO foundTask = taskService.getTaskById("1");

        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getTitle());
        verify(taskRepository, times(1)).findById("1");
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById("1"));
        verify(taskRepository, times(1)).findById("1");
    }

    @Test
    void testCreateTask() {
        when(taskRepository.findByTitle("Test Task")).thenReturn(Optional.empty());
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDTO);

        TaskDTO createdTask = taskService.createTask(taskRequest);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        verify(taskRepository, times(1)).findByTitle("Test Task");
        verify(taskRepository, times(1)).save(taskCaptor.capture());

        Task savedTask = taskCaptor.getValue();
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals("Test Description", savedTask.getDescription());
    }


    @Test
    void testUpdateTask() {
        when(taskRepository.findById("1")).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDTO);

        TaskDTO updatedTask = taskService.updateTask("1", taskUpdateRequest);

        assertNotNull(updatedTask);
        assertEquals("Test Task", updatedTask.getTitle());
        verify(taskRepository, times(1)).findById("1");
        verify(taskRepository, times(1)).save(taskCaptor.capture());

        Task savedTask = taskCaptor.getValue();
        assertEquals("Updated Task", savedTask.getTitle());
        assertEquals("Updated Description", savedTask.getDescription());
    }

    @Test
    void testUpdateTask_NotFound() {
        when(taskRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask("1", taskUpdateRequest));
        verify(taskRepository, times(1)).findById("1");
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById("1")).thenReturn(Optional.of(task));

        String response = taskService.deleteTask("1");

        assertEquals("Deleted task with id: 1", response);
        verify(taskRepository, times(1)).findById("1");
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask("1"));
        verify(taskRepository, times(1)).findById("1");
    }

    @Test
    @Rollback
    void testCreateTask_Successful() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.of(2024, 4, 7));
        task.setResponsible("John Doe");

        when(taskRepository.findByTitle(taskRequest.getTitle())).thenReturn(Optional.empty());
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(new TaskDTO());

        taskService.createTask(taskRequest);

        verify(taskRepository, times(1)).save(taskCaptor.capture());
        verify(taskHistoryRepository, times(1)).save(taskHistoryCaptor.capture());

        Task savedTask = taskCaptor.getValue();
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals("Test Description", savedTask.getDescription());
        assertEquals(LocalDate.of(2024, 4, 7), savedTask.getDueDate());
        assertEquals("John Doe", savedTask.getResponsible());

        TaskHistory savedTaskHistory = taskHistoryCaptor.getValue();
        assertEquals(task, savedTaskHistory.getTask());
    }

    @Test
    @Rollback
    void testCreateTask_RollbackOnFailure() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.of(2024, 4, 7));
        task.setResponsible("John Doe");

        when(taskRepository.findByTitle(taskRequest.getTitle())).thenReturn(Optional.empty());
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        doThrow(new RuntimeException("TaskHistory save failed")).when(taskHistoryRepository).save(any(TaskHistory.class));

        assertThrows(RuntimeException.class, () -> taskService.createTask(taskRequest));

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskHistoryRepository, times(1)).save(any(TaskHistory.class));

        // Verify that the taskRepository.save is called but the task is not persisted due to rollback
        verify(taskRepository, never()).findById(any(String.class)); // Add this to ensure that no further interactions occur
    }

    @Test
    @Rollback
    void testCreateTask_DuplicateTitle() {
        when(taskRepository.findByTitle(taskRequest.getTitle())).thenReturn(Optional.of(new Task()));

        assertThrows(DuplicateTaskException.class, () -> taskService.createTask(taskRequest));

        verify(taskRepository, times(1)).findByTitle(taskRequest.getTitle());
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskHistoryRepository, never()).save(any(TaskHistory.class));
    }
    @Test
    void testGetAllTasks() {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setId("1");
        taskHistory.setTitle("Test Task History");
        taskHistory.setDescription("Test Description History");
        taskHistory.setDueDate(LocalDate.of(2024, 4, 7));
        taskHistory.setResponsible("John Doe");
        taskHistory.setModifiedDate(LocalDateTime.now());

        TaskHistoryDTO taskHistoryDTO = new TaskHistoryDTO();
        taskHistoryDTO.setId("1");
        taskHistoryDTO.setTitle("Test Task History");
        taskHistoryDTO.setDescription("Test Description History");
        taskHistoryDTO.setDueDate(LocalDate.of(2024, 4, 7));
        taskHistoryDTO.setResponsible("John Doe");
        taskHistoryDTO.setModifiedDate(LocalDateTime.now());

        task.setTaskHistoryList(List.of(taskHistory));
        taskDTO.setTaskHistoryList(List.of(taskHistoryDTO));

        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);
        when(taskMapper.toHistoryDto(taskHistory)).thenReturn(taskHistoryDTO);

        List<TaskDTO> tasks = taskService.getAllTasks(null, null);

        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        assertEquals(1, tasks.get(0).getTaskHistoryList().size());
        assertEquals("Test Task History", tasks.get(0).getTaskHistoryList().get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }
}
