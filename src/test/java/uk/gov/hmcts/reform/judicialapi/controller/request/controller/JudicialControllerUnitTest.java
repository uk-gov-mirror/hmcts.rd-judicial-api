package uk.gov.hmcts.reform.judicialapi.controller.request.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialapi.controller.JudicialController;
import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeListResponse;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;
import uk.gov.hmcts.reform.judicialapi.service.JudicialRoleTypeService;

@Slf4j
public class JudicialControllerUnitTest {

    @InjectMocks
    private JudicialController judicialController;

    private JudicialRoleType judicialRoleTypeMock;
    private JudicialRoleTypeListResponse judicialRoleTypeListResponseMock;
    private JudicialRoleTypeService judicialRoleTypeServiceMock;

    @Before
    public void setUp() throws Exception {
        judicialRoleTypeMock = mock(JudicialRoleType.class);
        judicialRoleTypeListResponseMock = mock(JudicialRoleTypeListResponse.class);
        judicialRoleTypeServiceMock = mock(JudicialRoleTypeService.class);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveJudicialRoleTypes() {

        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        when(judicialRoleTypeServiceMock.retrieveJudicialRoles()).thenReturn(judicialRoleTypeListResponseMock);
        ResponseEntity<?> actual = judicialController.getJudicialRoles();
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
    }
}
