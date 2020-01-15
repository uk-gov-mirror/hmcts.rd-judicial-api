package uk.gov.hmcts.reform.judicialapi.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.judicialapi.controller.advice.ResourceNotFoundException;
import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeEntityResponse;
import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeResponse;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialRoleType;
import uk.gov.hmcts.reform.judicialapi.persistence.JudicialRoleTypeRepository;

public class JudicialRoleTypeServiceImplUnitTest {

    @InjectMocks
    private JudicialRoleTypeServiceImpl sut;

    private JudicialRoleTypeRepository judicialRoleTypeRepositoryMock;
    private JudicialRoleType judicialRoleTypeMock;

    private List<JudicialRoleType> judicialRoleTypes;

    @Before
    public void setUp() {

        judicialRoleTypeRepositoryMock = mock(JudicialRoleTypeRepository.class);
        judicialRoleTypeMock = mock(JudicialRoleType.class);
        MockitoAnnotations.initMocks(this);

        judicialRoleTypes = new ArrayList<>();
    }

    @Test
    public void retrieveJudicialRolesTest() {

        judicialRoleTypes.add(judicialRoleTypeMock);

        when(judicialRoleTypeRepositoryMock.findAll()).thenReturn(judicialRoleTypes);

        JudicialRoleTypeEntityResponse actual = sut.retrieveJudicialRoles();

        assertThat(actual).isNotNull();
        verify(judicialRoleTypeRepositoryMock, times(1)).findAll();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void retrieveJudicialRolesThrowsResourceNotFoundIfEmpty() {
        sut.retrieveJudicialRoles();
    }
}
