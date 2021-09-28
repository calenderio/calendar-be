package com.io.fastmeet.mappers;

import com.io.fastmeet.entitites.Todo;
import com.io.fastmeet.models.requests.todo.TodoUpdateRequest;
import com.io.fastmeet.models.responses.TodoCreateResponse;
import org.mapstruct.Mapper;

@Mapper
public interface TodoMapper {

    TodoCreateResponse mapToEntityModel(Todo todo);

    TodoUpdateRequest  entityToModel(Todo todo);
}
