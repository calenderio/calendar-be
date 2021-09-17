/*
 * @author : Oguz Kahraman
 * @since : 16.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.core.services;

import com.io.fastmeet.entitites.Cache;
import com.io.fastmeet.repositories.CacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class CacheService {

    @Autowired
    private CacheRepository repository;

    @Resource
    private CacheService self;


    @Cacheable(value = "CACHES")
    public Map<String, Cache> getCacheValues() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toMap(Cache::getName, Function.identity()));
    }

    public int getIntegerCacheValue(String cacheKey) {
        return self.getCacheValues().get(cacheKey) != null ? Integer.parseInt(self.getCacheValues().get(cacheKey).getValue()) : 0;
    }
}
