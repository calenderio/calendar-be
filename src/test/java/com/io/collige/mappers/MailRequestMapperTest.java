/*
 * @author : Oguz Kahraman
 * @since : 10.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.mappers;

import com.io.collige.entitites.Invitation;
import com.io.collige.models.internals.mail.GenericMailRequest;
import com.io.collige.models.requests.meet.SendInvitationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MailRequestMapperTest {

    private final MailRequestMapper mapper = Mappers.getMapper(MailRequestMapper.class);

    @Test
    void setInviteEmails() {
        Invitation invitation = new Invitation();
        invitation.setUserEmail("example@example.com");
        GenericMailRequest genericMailRequest = new GenericMailRequest();
        mapper.setInviteEmails(invitation, genericMailRequest);
        assertEquals(invitation.getUserEmail(), genericMailRequest.getEmails().iterator().next());
    }

    @Test
    void setEmails() {
        SendInvitationRequest request = new SendInvitationRequest();
        request.setUserMail("example@example.com");
        GenericMailRequest genericMailRequest = new GenericMailRequest();
        mapper.setEmails(request, genericMailRequest);
        assertEquals(request.getUserMail(), genericMailRequest.getEmails().iterator().next());
    }
}