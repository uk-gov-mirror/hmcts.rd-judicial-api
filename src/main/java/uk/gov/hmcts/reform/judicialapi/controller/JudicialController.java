package uk.gov.hmcts.reform.judicialapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.judicialapi.controller.response.JudicialRoleTypeListResponse;
import uk.gov.hmcts.reform.judicialapi.service.JudicialRoleTypeService;
import uk.gov.hmcts.reform.judicialapi.service.impl.JudicialRoleTypeServiceImpl;

@Api(value = "/v1/judicial")
@RequestMapping(path = "refdata/v1/judicial")
@RestController
@Slf4j
@NoArgsConstructor
public class JudicialController {

    @Autowired
    protected JudicialRoleTypeService judicialRoleTypeService;

    @ApiOperation(
            value = "Retrieves all judicial roles"
    )

    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "List of judicial role types",
                    response = JudicialRoleTypeListResponse.class
            ),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden Error: Access denied"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Server Error",
                    response = String.class
            )
    })

    @Secured("caseworker")
    @GetMapping(path = "/roles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getJudicialRoles() {
        Object judicialRolesResponse = judicialRoleTypeService.retrieveJudicialRoles();
        return ResponseEntity
                .status(200)
                .body(judicialRolesResponse);
    }
}
