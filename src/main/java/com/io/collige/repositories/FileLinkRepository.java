/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.repositories;

import com.io.collige.entitites.FileLink;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FileLinkRepository extends CrudRepository<FileLink, Long> {

    Optional<FileLink> findByIdAndUserId(Long id, Long userId);

    long countByUserId(Long userId);

}
