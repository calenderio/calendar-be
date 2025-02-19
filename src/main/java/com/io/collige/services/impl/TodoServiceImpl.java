package com.io.collige.services.impl;


import com.io.collige.constants.GeneralMessageConstants;
import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.i18n.Translator;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.entitites.Todo;
import com.io.collige.entitites.User;
import com.io.collige.mappers.TodoMapper;
import com.io.collige.models.internals.todo.FindToDoRequest;
import com.io.collige.models.requests.todo.TodoCreateRequest;
import com.io.collige.models.requests.todo.TodoUpdateRequest;
import com.io.collige.models.responses.todo.TodoDetails;
import com.io.collige.repositories.TodoRepository;
import com.io.collige.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private TodoMapper todoMapper;

    @Override
    public List<Todo> findTodosByUserId(FindToDoRequest request) {
        User user = jwtService.getLoggedUser();
        Pageable paging = PageRequest.of(request.getPageNo() - 1, request.getPageSize(), Sort.by(request.getSortBy()));

        Page<Todo> pagedResult = todoRepository.findTodosByUserId(user.getId(), paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public TodoDetails saveTodo(TodoCreateRequest todoCreateRequest) {
        User user = jwtService.getLoggedUser();

        Todo todo = new Todo();
        todo.setUserId(user.getId());
        todo.setDescription(todoCreateRequest.getDescription());
        todo.setPriority(todoCreateRequest.getPriority());
        todoRepository.save(todo);

        return todoMapper.mapToEntityModel(todo);
    }


    @Override
    public void deleteTodo(TodoUpdateRequest request) {
        User user = jwtService.getLoggedUser();
        long itemNumber = todoRepository.deleteByIdAndUserId(request.getId(), user.getId());
        if (itemNumber == 0) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.TODO_NOT_FOUND),
                    GeneralMessageConstants.TDO_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void setDone(TodoUpdateRequest request) {
        User user = jwtService.getLoggedUser();
        long itemNumber = todoRepository.setDone(request.getId(), user.getId());
        if (itemNumber == 0) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.TODO_NOT_FOUND),
                    GeneralMessageConstants.TDO_NOT_FOUND);
        }
    }

}
