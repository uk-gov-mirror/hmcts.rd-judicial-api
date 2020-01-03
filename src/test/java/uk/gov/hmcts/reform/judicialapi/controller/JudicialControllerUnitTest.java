package uk.gov.hmcts.reform.judicialapi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeResponse;
import uk.gov.hmcts.reform.judicialapi.service.JudicialRoleTypeService;

@Slf4j
public class JudicialControllerUnitTest {

    @InjectMocks
    private JudicialController judicialController;

    private JudicialRoleTypeResponse judicialRoleTypeResponseMock;
    private JudicialRoleTypeService judicialRoleTypeServiceMock;
    private List<JudicialRoleTypeResponse> judicialRoleTypeResponseList;

    @Before
    public void setUp() throws Exception {
        judicialRoleTypeResponseMock = mock(JudicialRoleTypeResponse.class);
        judicialRoleTypeServiceMock = mock(JudicialRoleTypeService.class);

        judicialRoleTypeResponseList = new ArrayList<>();
        judicialRoleTypeResponseList.add(judicialRoleTypeResponseMock);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveJudicialRoleTypes() {

        final HttpStatus expectedHttpStatus = HttpStatus.OK;

        when(judicialRoleTypeServiceMock.retrieveJudicialRoles()).thenReturn(judicialRoleTypeResponseList);

        ResponseEntity<List<JudicialRoleTypeResponse>> actual = judicialController.getJudicialRoles();

        verify(judicialRoleTypeServiceMock, times(1)).retrieveJudicialRoles();

        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatus);
    }
}
