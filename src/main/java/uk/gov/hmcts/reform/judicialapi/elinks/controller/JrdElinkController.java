package uk.gov.hmcts.reform.judicialapi.elinks.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.judicialapi.controller.request.UserSearchRequest;
import uk.gov.hmcts.reform.judicialapi.elinks.response.UserSearchResponseWrapper;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ElinkUserService;
import uk.gov.hmcts.reform.judicialapi.versions.V2;

import javax.validation.Valid;

import static uk.gov.hmcts.reform.judicialapi.util.RefDataConstants.BAD_REQUEST;


@RequestMapping(
    path = "/refdata/judicial/users"
)
@RestController
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class JrdElinkController {


    @Autowired
    ElinkUserService elinkUserService;

    @Operation(
        summary = "This endpoint will be used for user search based on partial query. When the consumers "
            + "inputs any 3 characters, they will call this api to fetch "
            + "the required result.",
        description = "**Valid IDAM role is required to access this endpoint**",
        security = {
            @SecurityRequirement(name = "Authorization"),
            @SecurityRequirement(name = "ServiceAuthorization")
        }
    )

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Retrieve the user profiles for the given request. ",
        content = @Content(schema = @Schema(implementation = UserSearchResponseWrapper.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = BAD_REQUEST,
        content = @Content
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "401",
        description = "User Authentication Failed",
        content = @Content
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "403",
        description = "Unauthorized",
        content = @Content
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content
    )

    @PostMapping(
        path = "/search",
        consumes = V2.MediaType.SERVICE,
        produces = V2.MediaType.SERVICE
    )
    public ResponseEntity<Object> retrieveUsers(@Valid @RequestBody UserSearchRequest userSearchRequest) {
        return elinkUserService.retrieveElinkUsers(userSearchRequest);
    }


}
