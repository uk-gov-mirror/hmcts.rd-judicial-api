package uk.gov.hmcts.reform.judicialapi.controller.response;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
@Slf4j
public class JudicialRoleTypeListResponseUnitTest {

    private JudicialRoleType judicialRoleTypeMock = mock(JudicialRoleType.class);

    @Test
    public void judicialRoleTypeListResponseUnitTest() {
        List<JudicialRoleType> judicialRoleTypes = new ArrayList<>();
        judicialRoleTypes.add(judicialRoleTypeMock);

        JudicialRoleTypeListResponse judicialRoleTypeListResponse = new JudicialRoleTypeListResponse(judicialRoleTypes);

        assertThat(judicialRoleTypeListResponse).isNotNull();
    }
}
