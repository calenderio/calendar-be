package com.io.collige.entitites;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.io.collige.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Table(name = "Todos", indexes = {
        @Index(name = "idx_todo_user_id", columnList = "user_id")
})
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Todo extends BaseEntity {



    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private boolean done;
    private LocalDateTime createDate;


}
