package com.taskmanager.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private Date dueDate;
    private String responsible;
    private List<SubtaskDTO> subTasks;
    private List<CommentDTO> comments;
}
