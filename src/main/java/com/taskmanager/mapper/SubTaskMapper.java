package com.taskmanager.mapper;

import com.taskmanager.dto.SubtaskDTO;
import com.taskmanager.dto.SubTaskRequest;
import com.taskmanager.model.SubTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TaskMapper.class})
public interface SubTaskMapper {

    @Mapping(source = "parentTask.id", target = "parentTaskId")
    SubtaskDTO toDto(SubTask subTask);

    @Mapping(target = "parentTask", ignore = true)
    SubTask toEntityFromRequest(SubTaskRequest subTaskRequest);

    @Mapping(target = "parentTask", ignore = true)
    SubTask toEntity(SubtaskDTO subtaskDTO);
}
