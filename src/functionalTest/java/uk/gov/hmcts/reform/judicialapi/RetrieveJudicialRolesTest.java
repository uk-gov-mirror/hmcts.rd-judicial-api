package uk.gov.hmcts.reform.judicialapi;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;

import java.nio.file.Paths;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

@RunWith(SpringIntegrationSerenityRunner.class)
@ActiveProfiles("functional")
@Slf4j
public class RetrieveJudicialRolesTest extends AuthorizationFunctionalTest {

    @BeforeClass
    public static void dbSetup() throws Exception {
        String loadFile = ResourceUtils.getFile("classpath:load-data-functional.sql").getCanonicalPath();
        String deleteFile = ResourceUtils.getFile("classpath:delete-data-functional.sql").getCanonicalPath();
        executeScript(ImmutableList.of(Paths.get(deleteFile), Paths.get(loadFile)));
    }

    @Test
    public void rdcc_739_ac1_user_with_appropriate_rights_can_retrieve_list_of_all_judicial_roles() {
        //Given I am a user with the appropriate rights to retrieve list of all Judicial Roles
        //When I search for the list of all Judicial Roles
        //Then I should be able to see the list of Judicial roles
        Map<String, Object> response = judicialApiClient.retrieveAllJudicialRoles("caseworker", HttpStatus.OK);
        assertThat(response).isNotNull();
        assertThat(response).containsValues("Magistrate", "Advisory Committee Member - Magistrate");
    }

    @Test
    public void rdcc_739_ac2_user_without_appropriate_rights_cannot_retrieve_list_of_all_judicial_roles() {
        //Given I am a user without the appropriate rights to retrieve list of all Judicial Roles
        //When I search for the list of all Judicial Roles
        //Then I should get a failure outcome as I do not have the appropriate rights for accessing role information
        Map<String, Object> response = judicialApiClient.retrieveAllJudicialRoles("pui-user-manager", HttpStatus.FORBIDDEN);
        assertThat(response.get("errorMessage")).isNotNull();
        assertThat(response.get("errorMessage")).isEqualTo("9 : Access Denied");
    }

    @AfterClass
    public static void dbTearDown() throws Exception {
        String deleteFile = ResourceUtils.getFile("classpath:delete-data-functional.sql").getCanonicalPath();
        executeScript(ImmutableList.of(Paths.get(deleteFile)));
    }

}
