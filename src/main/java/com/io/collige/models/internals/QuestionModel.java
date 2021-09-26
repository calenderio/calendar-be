/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import com.io.collige.enums.QuestionType;
import com.io.collige.validators.QuestionCheck;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@QuestionCheck
public class QuestionModel {

    @Schema(description = "Question text", example = "Example Question", required = true)
    @NotNull
    private String text;
    @Schema(description = "Question Type", example = "TEXT", required = true)
    @NotNull
    private QuestionType type;
    @Schema(description = "Question values if enu,", example = "EXAMPLE,EXAMPLE2", required = true)
    private String values;
    @Schema(description = "If text min length, if number min number", example = "1", required = true)
    private Integer lengthMin;
    @Schema(description = "If text max length, if number max number", example = "2", required = true)
    private Integer lengthMax;
    @Schema(description = "Question is required", example = "true", required = true)
    @NotNull
    private Boolean required;

}
