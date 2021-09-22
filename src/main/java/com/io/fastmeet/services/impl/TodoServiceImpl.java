package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.entitites.Todo;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.repositories.TodoRepository;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public class TodoServiceImpl implements TodoService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    JWTService jwtService;

    @Override
    public List<Todo> findTodosByUserId(Integer pageNo, Integer pageSize, String sortBy) {

        return null;
    }

    @Override
    public void saveTodo(Long id, String token) {

    }

    @Override
    public void deleteTodo(Long id, String token) {
        Optional<User> user = userRepository.findById(jwtService.getUserFromToken(token).getCompanyId());
    }
}
