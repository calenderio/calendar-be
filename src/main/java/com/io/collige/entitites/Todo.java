package com.io.collige.entitites;

import com.io.collige.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "todos")
@EqualsAndHashCode(callSuper = true)
public class Todo extends BaseEntity {

    private Long userId;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private boolean done = false;
    private LocalDateTime createDate = LocalDateTime.now();

}
