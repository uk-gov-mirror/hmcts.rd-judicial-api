package uk.gov.hmcts.reform.judicialapi.controller.domain;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialapi.domain.Authorisation;
import uk.gov.hmcts.reform.judicialapi.domain.UserProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthorisationTest {

    private static final LocalDate date = LocalDate.now();
    private static final LocalDateTime dateTime = LocalDateTime.now();

    @Test
    void testAuthorisation() {
        Authorisation authorisation = createAuthorisation();

        assertNotNull(authorisation);
        assertEquals(2L, authorisation.getOfficeAuthId());
        assertEquals("Languages", authorisation.getJurisdiction());
        assertEquals(233432L,authorisation.getTicketId());
        assertNotNull(authorisation.getStartDate());
        assertNotNull(authorisation.getEndDate());
        assertNotNull(authorisation.getCreatedDate());
        assertNotNull(authorisation.getLastUpdated());
        assertEquals("Welsh", authorisation.getLowerLevel());
        assertNotNull(authorisation.getUserProfile());
    }

    public static Authorisation createAuthorisation() {
        Authorisation authorisation = new Authorisation();
        authorisation.setOfficeAuthId(2L);
        authorisation.setJurisdiction("Languages");
        authorisation.setTicketId(233432L);
        authorisation.setStartDate(dateTime);
        authorisation.setEndDate(dateTime);
        authorisation.setCreatedDate(dateTime);
        authorisation.setLastUpdated(dateTime);
        authorisation.setLowerLevel("Welsh");
        authorisation.setUserProfile(new UserProfile());

        return authorisation;
    }
}
