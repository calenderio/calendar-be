package com.io.collige.mappers;

import com.io.collige.entitites.Todo;
import com.io.collige.models.responses.todo.TodoCreateResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    TodoCreateResponse mapToEntityModel(Todo todo);

}
