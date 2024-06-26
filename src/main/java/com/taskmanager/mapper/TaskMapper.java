package com.taskmanager.mapper;

import com.taskmanager.dto.TaskDTO;
import com.taskmanager.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SubTaskMapper.class})
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskDTO dto);

    TaskDTO toDto(Task entity);
}
