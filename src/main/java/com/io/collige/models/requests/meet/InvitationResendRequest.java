/*
 * @author : Oguz Kahraman
 * @since : 9.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.requests.meet;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
public class InvitationResendRequest {

    @ArraySchema(schema = @Schema(description = "Mail request bcc users for meeting", example = "1"))
    private Set<Long> fileIdList;

}
