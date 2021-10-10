/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals.event;

import com.io.collige.validators.QuestionCheck;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@QuestionCheck
public class QuestionModelResponse extends QuestionModel {

    @Schema(description = "Question text", example = "Example Question", required = true)
    private Long id;

}
