package com.taskmanager.service;

import com.taskmanager.dto.SubTaskRequest;
import com.taskmanager.dto.TaskDTO;
import com.taskmanager.dto.TaskRequest;
import com.taskmanager.exception.DuplicateTaskException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.SubTaskMapper;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.repository.SubTaskRepository;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final SubTaskRepository subTaskRepository;
    private final SubTaskMapper subTaskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, SubTaskRepository subTaskRepository, SubTaskMapper subTaskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.subTaskRepository = subTaskRepository;
        this.subTaskMapper = subTaskMapper;
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

        // Determine the query based on the provided filters
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

        // Map the Task entities to TaskDTOs
        return tasks.stream()
                .map(taskMapper::toDto)
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
    public TaskDTO createTask(TaskRequest taskRequest) {
        taskRepository.findByTitle(taskRequest.getTitle())
                .ifPresent(task -> {
                    throw new DuplicateTaskException("Task with title " + taskRequest.getTitle() + " already exists.");
                });

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setResponsible(taskRequest.getResponsible());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    /**
     * Updates an existing task.
     *
     * @param id      The ID of the task to update.
     * @param taskDTO The data transfer object representing the updated task.
     * @return An Optional containing the updated TaskDTO if the update was successful.
     * @throws ResourceNotFoundException if the task is not found.
     */
    public Optional<TaskDTO> updateTask(String id, TaskDTO taskDTO) {
        return Optional.ofNullable(taskRepository.findById(id).map(existingTask -> {
            // Map the DTO to an entity and update its ID to the existing task's ID
            Task task = taskMapper.toEntity(taskDTO);
            task.setId(existingTask.getId());

            // Update parent references for subtasks
            assert task.getSubTasks() != null;
            for (SubTask subTask : task.getSubTasks()) {
                subTask.setParentTask(task);
            }

            // Keep only the subtasks that are still present in the updated task
            List<String> subTaskIds = task.getSubTasks().stream()
                    .map(SubTask::getId)
                    .toList();
            existingTask.getSubTasks().removeIf(subTask -> !subTaskIds.contains(subTask.getId()));

            // Update the existing task with new values
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setResponsible(task.getResponsible());
            existingTask.setSubTasks(task.getSubTasks());

            // Save the updated task and return the DTO
            return taskMapper.toDto(taskRepository.save(existingTask));
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id)));
    }

    /**
     * Adds a new subtask to an existing task.
     *
     * @param taskId         The ID of the task to which the subtask is to be added.
     * @param subTaskRequest The data transfer object representing the new subtask.
     * @return The updated TaskDTO with the new subtask.
     * @throws ResourceNotFoundException if the task is not found.
     */
    public TaskDTO addSubtask(String taskId, SubTaskRequest subTaskRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));

        // Map the SubTaskRequest to SubTask entity
        SubTask subTask = subTaskMapper.toEntityFromRequest(subTaskRequest);
        subTask.setParentTask(task);

        // Save the subtask and update the parent task
        subTask = subTaskRepository.save(subTask);
        task.getSubTasks().add(subTask);
        taskRepository.save(task);

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
    public String deleteTask(String id) {
        return taskRepository.findById(id).map(task -> {
            taskRepository.delete(task);
            return "Deleted task with id: " + id;
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }
}
