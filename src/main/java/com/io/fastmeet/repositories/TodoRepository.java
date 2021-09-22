package com.io.fastmeet.repositories;

import com.io.fastmeet.entitites.Todo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends PagingAndSortingRepository <Todo , Long>{

    List<Todo> findByUserId();

}
