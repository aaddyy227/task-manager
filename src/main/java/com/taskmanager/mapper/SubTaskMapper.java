package com.taskmanager.mapper;

import com.taskmanager.dto.SubtaskDTO;
import com.taskmanager.model.SubTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubTaskMapper {
    @Mapping(target = "id", ignore = true)
    SubTask toEntity(SubtaskDTO dto);

    SubtaskDTO toDto(SubTask entity);
}
