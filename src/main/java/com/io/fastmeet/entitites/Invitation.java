/*
 * @author : Oguz Kahraman
 * @since : 17.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.entitites;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Data
@Table(name = "invitations")
public class Invitation extends BaseEntity {

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private String userEmail;
    private Long userId;
    private String name;
    private String invitationId;
    private Boolean scheduled = Boolean.FALSE;

}
