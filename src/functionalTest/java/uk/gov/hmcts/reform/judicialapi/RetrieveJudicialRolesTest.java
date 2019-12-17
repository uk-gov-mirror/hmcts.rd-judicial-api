package uk.gov.hmcts.reform.judicialapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@RunWith(SpringIntegrationSerenityRunner.class)
@ActiveProfiles("functional")
@Slf4j
public class RetrieveJudicialRolesTest extends AuthorizationFunctionalTest {

    @Test
    public void rdcc_739_ac1_user_with_appropriate_rights_can_retrieve_list_of_all_judicial_roles() {
        //Given I am a user with the appropriate rights to retrieve list of all Judicial Roles
        //When I search for the list of all Judicial Roles
        //Then I should be able to see the list of Judicial roles
        Map<String, Object> response = retrieveAllJudicialRoles("caseworker", HttpStatus.OK);
        assertThat(response).isNotNull();
        assertThat(response).containsValues("Magistrate", "Advisory Committee Member - Magistrate");
    }

    @Test
    public void rdcc_739_ac2_user_without_appropriate_rights_cannot_retrieve_list_of_all_judicial_roles() {
        //Given I am a user without the appropriate rights to retrieve list of all Judicial Roles
        //When I search for the list of all Judicial Roles
        //Then I should get a failure outcome as I do not have the appropriate rights for accessing role information
        Map<String, Object> response = retrieveAllJudicialRoles("pui-user-manager", HttpStatus.FORBIDDEN);
        assertThat(response.get("errorMessage")).isNotNull();
        assertThat(response.get("errorMessage")).isEqualTo("9 : Access Denied");
    }

    private RequestSpecification withUnauthenticatedRequest() {
        return SerenityRest.given()
                .relaxedHTTPSValidation()
                .baseUri(judicialApiUrl)
                .header("Content-Type", APPLICATION_JSON_UTF8_VALUE)
                .header("Accepts", APPLICATION_JSON_UTF8_VALUE);
    }

    public Map<String, Object> retrieveAllJudicialRoles(String roleOfAccessor, HttpStatus expectedStatus) {
        Response response = withUnauthenticatedRequest()
                .body("")
                .get("/refdata/v1/judicial/roles")
                .andReturn();


        response.then()
                .assertThat()
                .statusCode(expectedStatus.value());

        return response.body().as(Map.class);
    }
}
