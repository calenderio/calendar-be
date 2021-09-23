package com.io.fastmeet.services.impl;


import com.io.fastmeet.constants.GeneralMessageConstants;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.entitites.Todo;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.models.requests.todo.TodoCreateRequest;
import com.io.fastmeet.models.requests.todo.TodoDeleteRequest;
import com.io.fastmeet.repositories.TodoRepository;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class TodoServiceImpl implements TodoService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    JWTService jwtService;

    @Override
    public List<Todo> findTodosByUserId(Integer pageNo, Integer pageSize, String sortBy) {
        User user = jwtService.getLoggedUser();
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Todo> pagedResult = todoRepository.findTodosByUserId(user,paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {

            return new ArrayList<Todo>();
        }

    }

    @Override
    public void saveTodo(TodoCreateRequest todoCreateRequest, String token) {
        User user = jwtService.getLoggedUser();

        Todo todo = new Todo();
        todo.setUserId(user);
        todo.setDescription(todoCreateRequest.getDescription());
        todo.setPriority(todoCreateRequest.getPriority());
        todo.setDone(false);
        todoRepository.save(todo);
    }

    @Override
    public void deleteTodo(String token, TodoDeleteRequest request) {
        User user = jwtService.getLoggedUser();

        Todo todo = todoRepository.findByUserIdAndId(user, request.getId()).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.TODO_NOT_FOUND),
                        GeneralMessageConstants.USR_NOT_FOUND));
        todoRepository.delete(todo);
    }
}
