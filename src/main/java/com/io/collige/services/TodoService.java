package com.io.collige.services;

import com.io.collige.entitites.Todo;
import com.io.collige.models.internals.todo.FindToDoRequest;
import com.io.collige.models.requests.todo.TodoCreateRequest;
import com.io.collige.models.requests.todo.TodoUpdateRequest;
import com.io.collige.models.responses.todo.TodoDetails;

import java.util.List;


public interface TodoService {

    List<Todo> findTodosByUserId(FindToDoRequest request);

    TodoDetails saveTodo(TodoCreateRequest todoCreateRequest);

    void deleteTodo(TodoUpdateRequest request);

    void setDone(TodoUpdateRequest request);
}
