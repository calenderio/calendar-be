/*
 * @author : Oguz Kahraman
 * @since : 10.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.constants.GeneralMessageConstants;
import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.i18n.Translator;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.entitites.Todo;
import com.io.collige.entitites.User;
import com.io.collige.enums.Priority;
import com.io.collige.mappers.TodoMapper;
import com.io.collige.models.internals.todo.FindToDoRequest;
import com.io.collige.models.requests.todo.TodoCreateRequest;
import com.io.collige.models.requests.todo.TodoUpdateRequest;
import com.io.collige.models.responses.todo.TodoDetails;
import com.io.collige.repositories.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private Page<Todo> todoPage;

    @InjectMocks
    private TodoServiceImpl todoService;

    private TodoMapper todoMapper = Mappers.getMapper(TodoMapper.class);

    @Test
    void findTodosByUserId() {
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(todoRepository.findTodosByUserId(any(), any())).thenReturn(Page.empty());
        List<Todo> response = todoService.findTodosByUserId(new FindToDoRequest(1, 1, "id"));
        assertEquals(new ArrayList<>(), response);
    }

    @Test
    void findTodosByUserId_filled() {
        Todo todo = new Todo();
        todo.setId(1L);
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(todoRepository.findTodosByUserId(any(), any())).thenReturn(todoPage);
        when(todoPage.hasContent()).thenReturn(true);
        when(todoPage.getContent()).thenReturn(Collections.singletonList(todo));
        List<Todo> response = todoService.findTodosByUserId(new FindToDoRequest(1, 1, "id"));
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());
    }

    @Test
    void saveTodo() {
        ReflectionTestUtils.setField(todoService, "todoMapper", todoMapper);
        User user = new User();
        user.setId(1L);
        TodoCreateRequest todoCreateRequest = new TodoCreateRequest();
        todoCreateRequest.setDescription("Example");
        todoCreateRequest.setPriority(Priority.CRITICAL);
        when(jwtService.getLoggedUser()).thenReturn(user);
        TodoDetails details = todoService.saveTodo(todoCreateRequest);
        assertEquals("Example", details.getDescription());
        assertEquals(Priority.CRITICAL, details.getPriority());
    }

    @Test
    void deleteTodo() {
        User user = new User();
        user.setId(1L);
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest();
        todoUpdateRequest.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(todoRepository.deleteByIdAndUserId(1L, 1L)).thenReturn(1L);
        todoService.deleteTodo(todoUpdateRequest);
        verify(todoRepository, times(1)).deleteByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    void deleteTodo_exception() {
        MockedStatic<Translator> translatorMockedStatic = Mockito.mockStatic(Translator.class);
        translatorMockedStatic.when(() -> Translator.getMessage(any())).thenReturn("");
        User user = new User();
        user.setId(1L);
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest();
        todoUpdateRequest.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(todoRepository.deleteByIdAndUserId(1L, 1L)).thenReturn(0L);
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> todoService.deleteTodo(todoUpdateRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(GeneralMessageConstants.TDO_NOT_FOUND, exception.getCause().getMessage());
        translatorMockedStatic.close();
    }

    @Test
    void setDone() {
        User user = new User();
        user.setId(1L);
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest();
        todoUpdateRequest.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(todoRepository.setDone(1L, 1L)).thenReturn(1L);
        todoService.setDone(todoUpdateRequest);
        verify(todoRepository, times(1)).setDone(anyLong(), anyLong());
    }

    @Test
    void setDone_exception() {
        MockedStatic<Translator> translatorMockedStatic = Mockito.mockStatic(Translator.class);
        translatorMockedStatic.when(() -> Translator.getMessage(any())).thenReturn("");
        User user = new User();
        user.setId(1L);
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest();
        todoUpdateRequest.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(todoRepository.setDone(1L, 1L)).thenReturn(0L);
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> todoService.setDone(todoUpdateRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(GeneralMessageConstants.TDO_NOT_FOUND, exception.getCause().getMessage());
        translatorMockedStatic.close();
    }

}