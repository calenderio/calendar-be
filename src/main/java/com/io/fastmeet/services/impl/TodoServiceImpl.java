package com.io.fastmeet.services.impl;

import com.io.fastmeet.entitites.Todo;
import com.io.fastmeet.services.TodoService;

import java.util.List;

public class TodoServiceImpl implements TodoService {
    @Override
    public List<Todo> findTodosByUserId(Integer pageNo, Integer pageSize, String sortBy) {

        return null;
    }

    @Override
    public void saveTodo(Long id) {

    }

    @Override
    public void deleteTodo(Long id) {

    }
}
