package com.taskmanager.service;

import com.taskmanager.dto.SubTaskRequest;
import com.taskmanager.dto.TaskDTO;
import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskUpdateRequest;
import com.taskmanager.exception.DuplicateTaskException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.SubTaskMapper;
import com.taskmanager.repository.TaskHistoryRepository;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskHistory;
import com.taskmanager.repository.SubTaskRepository;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final SubTaskRepository subTaskRepository;
    private final SubTaskMapper subTaskMapper;
    private final TaskHistoryRepository taskHistoryRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, SubTaskRepository subTaskRepository, SubTaskMapper subTaskMapper, TaskHistoryRepository taskHistoryRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.subTaskRepository = subTaskRepository;
        this.subTaskMapper = subTaskMapper;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    /**
     * Retrieves all tasks, optionally filtered by title and/or responsible.
     *
     * @param title       The title of the tasks to filter by (optional).
     * @param responsible The responsible person for the tasks to filter by (optional).
     * @return A list of TaskDTOs matching the filter criteria.
     * @throws ResourceNotFoundException if no tasks are found for the given criteria.
     */
    public List<TaskDTO> getAllTasks(String title, String responsible) {
        List<Task> tasks;

        if (title != null && responsible != null) {
            tasks = taskRepository.findByTitleAndResponsible(title, responsible)
                    .orElseThrow(() -> new ResourceNotFoundException("Tasks not found with title " + title + " and responsible " + responsible));
        } else if (title != null) {
            tasks = taskRepository.findAllByTitle(title)
                    .orElseThrow(() -> new ResourceNotFoundException("Tasks not found with title " + title));
        } else if (responsible != null) {
            tasks = taskRepository.findByResponsible(responsible)
                    .orElseThrow(() -> new ResourceNotFoundException("Tasks not found with responsible " + responsible));
        } else {
            tasks = taskRepository.findAll();
            if (tasks.isEmpty()) {
                throw new ResourceNotFoundException("No tasks found");
            }
        }

        return tasks.stream()
                .map(task -> {
                    TaskDTO taskDTO = taskMapper.toDto(task);
                    taskDTO.setTaskHistoryList(task.getTaskHistoryList().stream()
                            .map(taskMapper::toHistoryDto)
                            .collect(Collectors.toList()));
                    return taskDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The TaskDTO corresponding to the given ID.
     * @throws ResourceNotFoundException if the task is not found.
     */
    public TaskDTO getTaskById(String id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    /**
     * Creates a new task.
     *
     * @param taskRequest The data transfer object representing the task to create.
     * @return The created TaskDTO.
     * @throws DuplicateTaskException if a task with the same title already exists.
     */
    @Transactional
    public TaskDTO createTask(TaskRequest taskRequest) {
        taskRepository.findByTitle(taskRequest.getTitle())
                .ifPresent(task -> {
                    throw new DuplicateTaskException("Task with title " + taskRequest.getTitle() + " already exists.");
                });

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setResponsible(taskRequest.getResponsible());

        task = taskRepository.save(task);

        saveTaskHistory(task);

        return taskMapper.toDto(task);
    }

    /**
     * Updates an existing task.
     *
     * @param id                The ID of the task to update.
     * @param taskUpdateRequest The data transfer object representing the updated task.
     * @return The updated TaskDTO if the update was successful.
     * @throws ResourceNotFoundException if the task is not found.
     */
    @Transactional
    public TaskDTO updateTask(String id, TaskUpdateRequest taskUpdateRequest) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));

        // Save current state to history before making changes
        saveTaskHistory(existingTask);

        // Update task fields
        existingTask.setTitle(taskUpdateRequest.getTitle());
        existingTask.setDescription(taskUpdateRequest.getDescription());
        existingTask.setDueDate(taskUpdateRequest.getDueDate());
        existingTask.setResponsible(taskUpdateRequest.getResponsible());

        // Save updated task and convert to DTO
        Task savedTask = taskRepository.save(existingTask);
        return taskMapper.toDto(savedTask);
    }

    /**
     * Adds a new subtask to an existing task.
     *
     * @param taskId         The ID of the task to which the subtask is to be added.
     * @param subTaskRequest The data transfer object representing the new subtask.
     * @return The updated TaskDTO with the new subtask.
     * @throws ResourceNotFoundException if the task is not found.
     * @throws DuplicateTaskException    if a subtask with the same title already exists in the task.
     */
    @Transactional
    public TaskDTO addSubtask(String taskId, SubTaskRequest subTaskRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));

        // Check for existing subtask with the same title within the task
        if (subTaskRepository.findByTitleAndParentTaskId(subTaskRequest.getTitle(), taskId).isPresent()) {
            throw new DuplicateTaskException("SubTask with title " + subTaskRequest.getTitle() + " already exists in task " + taskId);
        }

        // Map the SubTaskRequest to SubTask entity
        SubTask subTask = subTaskMapper.toEntityFromRequest(subTaskRequest);
        subTask.setParentTask(task);

        // Save the subtask and update the parent task
        subTask = subTaskRepository.save(subTask);
        task.getSubTasks().add(subTask);
        taskRepository.save(task);
        saveTaskHistory(task);
        // Return the updated TaskDTO
        return taskMapper.toDto(task);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @return A message indicating the task was deleted.
     * @throws ResourceNotFoundException if the task is not found.
     */
    @Transactional
    public String deleteTask(String id) {
        return taskRepository.findById(id).map(task -> {
            // Save current state to history before deletion
            saveTaskHistory(task);

            taskRepository.delete(task);
            return "Deleted task with id: " + id;
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    /**
     * Saves the current state of the given task to the history within a transactional context.
     *
     * @param task The task whose state is to be saved.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTaskHistory(Task task) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTask(task);
        taskHistory.setTitle(task.getTitle());
        taskHistory.setDescription(task.getDescription());
        taskHistory.setDueDate(task.getDueDate());
        taskHistory.setResponsible(task.getResponsible());
        taskHistory.setModifiedDate(LocalDateTime.now());
        taskHistoryRepository.save(taskHistory);
    }
}
