/*
 * @author : Oguz Kahraman
 * @since : 23.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionAnswerModel {

    private String question;
    private List<String> answer;

}
