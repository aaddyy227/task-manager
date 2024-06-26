package com.taskmanager.dto;


import com.taskmanager.model.SubTask;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.N;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TaskDTO {
    private String title;
    private String description;
    private Date dueDate;
    private String responsible;
    private List<SubtaskDTO> subTasks;
}
