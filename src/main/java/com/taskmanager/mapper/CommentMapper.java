package com.taskmanager.mapper;

import com.taskmanager.dto.CommentDTO;
import com.taskmanager.model.Comment;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toEntity(CommentDTO dto);

    CommentDTO toDto(Comment entity);
}
