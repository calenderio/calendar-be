package com.io.collige.controllers.impl;

import com.io.collige.controllers.TodoController;
import com.io.collige.entitites.Todo;
import com.io.collige.models.requests.todo.TodoCreateRequest;
import com.io.collige.models.requests.todo.TodoUpdateRequest;
import com.io.collige.models.responses.todo.TodoCreateResponse;
import com.io.collige.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TodoControllerImpl implements TodoController {

    @Autowired
    TodoService todoService;


    @Override
    public ResponseEntity<List<Todo>> getTodos(Integer pageNo, Integer pageSize, String sortBy) {
        List<Todo> toDoList = todoService.findTodosByUserId(pageNo, pageSize , sortBy);
        return new ResponseEntity<>(toDoList, new HttpHeaders(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TodoCreateResponse> createScheduler(TodoCreateRequest request) {
        TodoCreateResponse todoCreateResponse = todoService.saveTodo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(todoCreateResponse);
    }

    @Override
    public ResponseEntity<Void> deleteTodo(Long todoId) {
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest();
        todoUpdateRequest.setId(todoId);
        todoService.deleteTodo(todoUpdateRequest);
        return ResponseEntity.noContent().build() ;
    }

    @Override
    public ResponseEntity<Void> setDoneTodo(Long todoId) {
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest();
        todoUpdateRequest.setId(todoId);
        todoService.setDone(todoUpdateRequest);
        return ResponseEntity.noContent().build() ;
    }
}
