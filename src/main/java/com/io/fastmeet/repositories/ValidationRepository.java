/*
 * @author : Oguz Kahraman
 * @since : 11.02.2021
 *
 * Copyright - restapi
 **/
package com.io.fastmeet.repositories;


import com.io.fastmeet.entitites.Validation;
import com.io.fastmeet.enums.ValidationType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional
public interface ValidationRepository extends CrudRepository<Validation, Long> {

    Optional<Validation> findByCodeAndMailAndType(String code, String mail, ValidationType type);

    Optional<Validation> findByMailAndType(String mail, ValidationType type);

    @Query("DELETE FROM Validation val where val.date <= :time")
    @Modifying
    int deleteExRequests(LocalDateTime time);
}
