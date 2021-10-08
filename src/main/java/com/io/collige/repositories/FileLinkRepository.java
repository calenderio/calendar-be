/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.repositories;

import com.io.collige.entitites.FileLink;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FileLinkRepository extends CrudRepository<FileLink, Long> {

    Optional<FileLink> findByIdAndUserId(Long id, Long userId);

    long countByUserId(Long userId);

    List<FileLink> findByUserId(Long userId);

    List<FileLink> findByUserIdAndIdIn(Long userId, List<Long> ids);

    @Query(value = "select fl from FileLink fl inner join EventFileLink efl on efl.id.eventId = :eventId and  efl.id.fileId = fl.id " +
            "and efl.id.userId = :userId and fl.userId = :userId")
    List<FileLink> findByFileLink(Long userId, Long eventId);

}
