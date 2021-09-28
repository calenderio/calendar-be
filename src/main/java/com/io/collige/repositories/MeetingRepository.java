package com.io.collige.repositories;

import com.io.collige.entitites.Meeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface MeetingRepository extends CrudRepository<Meeting, String> {

    Optional<Meeting> findByInvitationId(Long invitationId);

    @Query("select m from Meeting  m where m.userId = :userId and :startDate <= m.startDate and :endDate >= m.endDate")
    List<Meeting> findUserMeetings(Long userId, ZonedDateTime startDate, ZonedDateTime endDate);

    @Query("select m from Meeting  m where m.eventId = :eventId and :startDate <= m.startDate and :endDate >= m.endDate")
    List<Meeting> findUserEventMeetings(Long eventId, LocalDateTime startDate, LocalDateTime endDate);
}
