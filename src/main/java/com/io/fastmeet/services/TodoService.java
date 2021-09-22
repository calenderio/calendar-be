package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Todo;
import java.util.List;


public interface TodoService {


    List<Todo> findTodosByUserId(Integer pageNo , Integer pageSize , String sortBy);

    void saveTodo(Long id, String token);

    void deleteTodo(Long id, String token);

}
