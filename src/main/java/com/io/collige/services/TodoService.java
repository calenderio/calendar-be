package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Todo;
import com.io.fastmeet.models.requests.todo.TodoCreateRequest;
import com.io.fastmeet.models.requests.todo.TodoUpdateRequest;
import com.io.fastmeet.models.responses.TodoCreateResponse;

import java.util.List;


public interface TodoService {


    List<Todo> findTodosByUserId(Integer pageNo , Integer pageSize , String sortBy);

    TodoCreateResponse saveTodo(TodoCreateRequest todoCreateRequest, String token);

    void deleteTodo(TodoUpdateRequest request);

    void setDone(TodoUpdateRequest request);
}
