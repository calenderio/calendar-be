/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.repositories;

import com.io.fastmeet.entitites.Invitation;
import com.io.fastmeet.entitites.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {

    Optional<Invitation> findByIdAndUserAndScheduledIsFalse(Long id, User user);

    List<Invitation> findByUserAndEvent_Id(User user, Long eventId);

    @Query("delete from Invitation i where i.user.id = :userId and i.id = :id and i.scheduled = false ")
    @Modifying
    int deleteInvitation(Long userId, Long id);

}
