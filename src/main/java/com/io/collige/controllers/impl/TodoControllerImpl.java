package com.io.collige.controllers.impl;

import com.io.collige.controllers.TodoController;
import com.io.collige.entitites.Todo;
import com.io.collige.mappers.TodoMapper;
import com.io.collige.models.requests.todo.TodoCreateRequest;
import com.io.collige.models.requests.todo.TodoUpdateRequest;
import com.io.collige.models.responses.todo.TodoDetails;
import com.io.collige.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TodoControllerImpl implements TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoMapper todoMapper;

    @Override
    public ResponseEntity<List<TodoDetails>> getTodos(Integer pageNo, Integer pageSize, String sortBy) {
        List<Todo> toDoList = todoService.findTodosByUserId(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(todoMapper.mapEntityListToModelList(toDoList));
    }

    @Override
    public ResponseEntity<TodoDetails> createTodo(@Valid TodoCreateRequest request) {
        TodoDetails todoDetails = todoService.saveTodo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(todoDetails);
    }

    @Override
    public ResponseEntity<Void> deleteTodo(Long todoId) {
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest();
        todoUpdateRequest.setId(todoId);
        todoService.deleteTodo(todoUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> setDoneTodo(Long todoId) {
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest();
        todoUpdateRequest.setId(todoId);
        todoService.setDone(todoUpdateRequest);
        return ResponseEntity.noContent().build();
    }

}
