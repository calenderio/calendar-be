package com.io.fastmeet.repositories;

import com.io.fastmeet.entitites.Licence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenceRepository extends CrudRepository<Licence , Long> {

    Optional<Licence> findByCompanyId(Long aLong);

}
