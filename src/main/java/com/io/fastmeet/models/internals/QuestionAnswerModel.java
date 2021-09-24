/*
 * @author : Oguz Kahraman
 * @since : 23.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionAnswerModel {

    private String question;
    private List<String> answer;

}
