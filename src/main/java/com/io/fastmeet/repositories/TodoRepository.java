package com.io.fastmeet.repositories;

import com.io.fastmeet.entitites.Todo;
import com.io.fastmeet.entitites.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends PagingAndSortingRepository <Todo , Long>{

    Optional<Todo> findByUserIdAndId(User user, Long todoId);

    Page<Todo> findTodosByUserId(User user, Pageable pageable);

}
