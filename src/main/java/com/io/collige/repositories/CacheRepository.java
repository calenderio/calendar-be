/*
 * @author : Oguz Kahraman
 * @since : 16.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.repositories;

import com.io.collige.entitites.Cache;
import org.springframework.data.repository.CrudRepository;

public interface CacheRepository extends CrudRepository<Cache, String> {
}
