package com.io.collige.mappers;

import com.io.collige.entitites.Todo;
import com.io.collige.models.responses.todo.TodoDetails;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    TodoDetails mapToEntityModel(Todo todo);

    List<TodoDetails> mapEntityListToModelList(List<Todo> todo);

}
