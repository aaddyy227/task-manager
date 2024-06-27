package com.taskmanager.mapper;

import com.taskmanager.dto.TaskDTO;
import com.taskmanager.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SubTaskMapper.class, CommentMapper.class})
public interface TaskMapper {
    @Mapping(target = "subTasks", source = "subTasks")
    @Mapping(target = "comments", source = "comments")
    Task toEntity(TaskDTO dto);

    @Mapping(target = "subTasks", source = "subTasks")
    @Mapping(target = "comments", source = "comments")
    TaskDTO toDto(Task entity);
}
