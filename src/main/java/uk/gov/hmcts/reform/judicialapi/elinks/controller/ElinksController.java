package uk.gov.hmcts.reform.judicialapi.elinks.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.judicialapi.elinks.response.ElinkBaseLocationWrapperResponse;
import uk.gov.hmcts.reform.judicialapi.elinks.response.ElinkLocationWrapperResponse;
import uk.gov.hmcts.reform.judicialapi.elinks.response.IdamResponse;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ELinksService;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ElinksPeopleService;
import uk.gov.hmcts.reform.judicialapi.elinks.service.IdamElasticSearchService;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.AUTHORIZATION;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.BAD_REQUEST;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.FORBIDDEN_ERROR;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.INTERNAL_SERVER_ERROR;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.NO_DATA_FOUND;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.TOO_MANY_REQUESTS;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.UNAUTHORIZED_ERROR;

@RestController
@RequestMapping(
        path = "/refdata/internal/elink"
)
@Slf4j
@AllArgsConstructor
@SuppressWarnings("all")
public class ElinksController {


    @Autowired
    ELinksService eLinksService;

    @Autowired
    ElinksPeopleService elinksPeopleService;

    @Autowired
    IdamElasticSearchService idamElasticSearchService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${elinksUrl}")
    String elinksUrl;

    @Value("${elinksApiKey}")
    private String elinksApiKey;


    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Get list of location and populate region type.",
                    response = ElinkLocationWrapperResponse.class
            ),
            @ApiResponse(
                    code = 400,
                    message = BAD_REQUEST
            ),
            @ApiResponse(
                    code = 401,
                    message = UNAUTHORIZED_ERROR
            ),
            @ApiResponse(
                    code = 403,
                    message = FORBIDDEN_ERROR
            ),
            @ApiResponse(
                    code = 404,
                    message = NO_DATA_FOUND
            ),
            @ApiResponse(
                    code = 429,
                    message = TOO_MANY_REQUESTS
            ),
            @ApiResponse(
                    code = 500,
                    message = INTERNAL_SERVER_ERROR
            )
    })
    @GetMapping (path = "/reference_data/location",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ElinkLocationWrapperResponse> loadLocation(){


        return eLinksService.retrieveLocation();
    }


    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Get list of Base locations and populate base location type.",
                    response = ElinkBaseLocationWrapperResponse.class
            ),
            @ApiResponse(
                    code = 400,
                    message = BAD_REQUEST
            ),
            @ApiResponse(
                    code = 401,
                    message = UNAUTHORIZED_ERROR
            ),
            @ApiResponse(
                    code = 403,
                    message = FORBIDDEN_ERROR
            ),
            @ApiResponse(
                    code = 404,
                    message = NO_DATA_FOUND
            ),
            @ApiResponse(
                    code = 429,
                    message = TOO_MANY_REQUESTS
            ),
            @ApiResponse(
                    code = 500,
                    message = INTERNAL_SERVER_ERROR
            )
    })
    @GetMapping(  path = "/reference_data/base_location",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ElinkBaseLocationWrapperResponse> loadBaseLocationType() {

        return eLinksService.retrieveBaseLocation();

    }

    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Get list of idam users.",
                    response = Object.class
            ),
            @ApiResponse(
                    code = 400,
                    message = BAD_REQUEST
            ),
            @ApiResponse(
                    code = 401,
                    message = UNAUTHORIZED_ERROR
            ),
            @ApiResponse(
                    code = 403,
                    message = FORBIDDEN_ERROR
            ),
            @ApiResponse(
                    code = 500,
                    message = INTERNAL_SERVER_ERROR
            )
    })
    @GetMapping (path = "/people",
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> loadPeople() {

        return elinksPeopleService.updatePeople();

    }

    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Get list of idam users.",
                    response = IdamResponse.class
            ),
            @ApiResponse(
                    code = 400,
                    message = BAD_REQUEST
            ),
            @ApiResponse(
                    code = 401,
                    message = UNAUTHORIZED_ERROR
            ),
            @ApiResponse(
                    code = 403,
                    message = FORBIDDEN_ERROR
            ),
            @ApiResponse(
                    code = 500,
                    message = INTERNAL_SERVER_ERROR
            )
    })
    @GetMapping (path = "/idam/elastic/search",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> idamElasticSearch() {

        Set<IdamResponse> response =  idamElasticSearchService.getIdamElasticSearchSyncFeed();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping (path = "/elinktest/{path}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> testingelinks(@PathVariable String path) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AUTHORIZATION, "Token " + elinksApiKey);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<Object> response = restTemplate.getForEntity(elinksUrl+path,Object.class,entity);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }


}
