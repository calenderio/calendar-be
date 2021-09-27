package com.io.fastmeet.entitites;

import com.io.fastmeet.enums.Priority;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.fortuna.ical4j.model.DateTime;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Todo extends BaseEntity{

    @ManyToOne
    private User userId;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @BooleanFlag
    private boolean isDone;

    private LocalDateTime createDateTime;


}
