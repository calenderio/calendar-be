/*
 * @author : Oguz Kahraman
 * @since : 23.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.requests.calendar;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class QuestionData {

    @ArraySchema(schema = @Schema(description = "Answer text", example = "Example answer", required = true))
    @Size(min = 1)
    private List<@NotBlank String> answerList;

    @Schema(description = "Question Id", example = "1", required = true)
    @NotNull
    private Long id;

}
