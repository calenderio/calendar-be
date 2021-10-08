/*
 * @author : Oguz Kahraman
 * @since : 9.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.enums.Priority;
import com.io.collige.mappers.TodoMapper;
import com.io.collige.models.requests.todo.TodoCreateRequest;
import com.io.collige.models.responses.todo.TodoDetails;
import com.io.collige.services.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoControllerImplTest {

    @Mock
    private TodoService todoService;

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoControllerImpl todoController;

    @Test
    void getTodos() {
        TodoDetails todo = new TodoDetails();
        todo.setPriority(Priority.CRITICAL);
        when(todoService.findTodosByUserId(1, 1, "id")).thenReturn(new ArrayList<>());
        when(todoMapper.mapEntityListToModelList(any())).thenReturn(Collections.singletonList(todo));
        ResponseEntity<List<TodoDetails>> listResponseEntity = todoController.getTodos(1, 1, "id");
        assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());
        assertFalse(Objects.requireNonNull(listResponseEntity.getBody()).isEmpty());
        assertEquals(Priority.CRITICAL, Objects.requireNonNull(listResponseEntity.getBody()).get(0).getPriority());
    }

    @Test
    void createTodo() {
        TodoDetails todo = new TodoDetails();
        todo.setPriority(Priority.CRITICAL);
        when(todoService.saveTodo(any())).thenReturn(todo);
        ResponseEntity<TodoDetails> listResponseEntity = todoController.createTodo(new TodoCreateRequest());
        assertEquals(HttpStatus.CREATED, listResponseEntity.getStatusCode());
        assertEquals(Priority.CRITICAL, Objects.requireNonNull(listResponseEntity.getBody()).getPriority());
    }

    @Test
    void deleteTodo() {
        ResponseEntity<Void> listResponseEntity = todoController.deleteTodo(1L);
        assertEquals(HttpStatus.NO_CONTENT, listResponseEntity.getStatusCode());
        verify(todoService, times(1)).deleteTodo(any());
    }

    @Test
    void setDoneTodo() {
        ResponseEntity<Void> listResponseEntity = todoController.setDoneTodo(1L);
        assertEquals(HttpStatus.NO_CONTENT, listResponseEntity.getStatusCode());
        verify(todoService, times(1)).setDone(any());
    }

}