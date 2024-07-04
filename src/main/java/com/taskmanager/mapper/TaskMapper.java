package com.taskmanager.mapper;

import com.taskmanager.dto.TaskDTO;
import com.taskmanager.dto.TaskHistoryDTO;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SubTaskMapper.class})
public interface TaskMapper {

    @Mapping(source = "subTasks", target = "subTasks")
    TaskDTO toDto(Task task);

    @Mapping(target = "subTasks", ignore = true) // To avoid recursion issues
    Task toEntity(TaskDTO taskDTO);
    TaskHistoryDTO toHistoryDto(TaskHistory taskHistory);

}
