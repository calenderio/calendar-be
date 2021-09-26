package com.io.fastmeet.repositories;

import com.io.fastmeet.entitites.Meeting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MeetingRepository extends CrudRepository<Meeting, String> {

    Optional<Meeting> findByInvitationId(Long invitationId);
}
