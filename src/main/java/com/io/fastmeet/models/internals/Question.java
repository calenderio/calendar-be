/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Question {

    @Schema(description = "Question text", example = "Example Question", required = true)
    @NotNull
    private String text;
    @Schema(description = "Question Type", example = "Textarea", required = true)
    @NotNull
    private String type;

}
