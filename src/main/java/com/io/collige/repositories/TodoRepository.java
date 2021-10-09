package com.io.collige.repositories;

import com.io.collige.entitites.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends PagingAndSortingRepository<Todo, Long> {

    Page<Todo> findTodosByUserId(Long userId, Pageable pageable);

    long deleteByIdAndUserId(Long id, Long userId);

    @Query("update Todo t set t.done = true where t.id = :id and t.userId = :userId")
    @Modifying
    long setDone(Long id, Long userId);

}