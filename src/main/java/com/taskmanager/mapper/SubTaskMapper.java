package com.taskmanager.mapper;

import com.taskmanager.dto.SubTaskRequest;
import com.taskmanager.dto.SubtaskDTO;
import com.taskmanager.model.SubTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubTaskMapper {
    @Mapping(target = "parentTask", ignore = true)
    @Mapping(target = "id", ignore = true) // Ignore id as it will be generated
    SubTask toEntity(SubtaskDTO dto);

    SubtaskDTO toDto(SubTask entity);

    // Add mapping for SubTaskRequest to SubTask
    @Mapping(target = "parentTask", ignore = true)
    @Mapping(target = "id", ignore = true)
    SubTask toEntityFromRequest(SubTaskRequest request);
}
