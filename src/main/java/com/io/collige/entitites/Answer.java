/*
 * @author : Oguz Kahraman
 * @since : 17.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.entitites;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Data
@Table(name = "answers")
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class Answer extends BaseEntity {

    @Type(type = "list-array")
    private List<String> text;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invite_id", nullable = false)
    private Invitation invitation;

}
