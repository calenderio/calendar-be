package com.io.collige.services;

import com.io.collige.entitites.Todo;
import com.io.collige.models.requests.todo.TodoCreateRequest;
import com.io.collige.models.requests.todo.TodoUpdateRequest;
import com.io.collige.models.responses.todo.TodoCreateResponse;

import java.util.List;


public interface TodoService {


    List<Todo> findTodosByUserId(Integer pageNo , Integer pageSize , String sortBy);

    TodoCreateResponse saveTodo(TodoCreateRequest todoCreateRequest, String token);

    void deleteTodo(TodoUpdateRequest request);

    void setDone(TodoUpdateRequest request);
}
