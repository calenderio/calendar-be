package com.io.fastmeet.repositories;

import com.io.fastmeet.entitites.Meeting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MeetingRepository extends CrudRepository<Meeting, String> {


}
