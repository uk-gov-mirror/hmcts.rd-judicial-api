
package uk.gov.hmcts.reform.judicialapi.controller.controller;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialapi.controller.WelcomeController;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WelcomeControllerTest {

    private final WelcomeController welcomeController = new WelcomeController();

    @Test
    void test_should_return_welcome_response() throws Exception {
        assertTrue(true);
    }
}

