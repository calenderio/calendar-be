package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Todo;
import com.io.fastmeet.models.requests.todo.TodoCreateRequest;
import com.io.fastmeet.models.requests.todo.TodoDeleteRequest;

import java.util.List;


public interface TodoService {


    List<Todo> findTodosByUserId(Integer pageNo , Integer pageSize , String sortBy);

    void saveTodo(TodoCreateRequest todoCreateRequest, String token);

    void deleteTodo(String token, TodoDeleteRequest request);

}
