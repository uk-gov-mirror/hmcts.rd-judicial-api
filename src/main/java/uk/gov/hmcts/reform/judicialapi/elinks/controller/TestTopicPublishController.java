package uk.gov.hmcts.reform.judicialapi.elinks.controller;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.BAD_REQUEST;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.FORBIDDEN_ERROR;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.INTERNAL_SERVER_ERROR;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.JOB_DETAILS_UPDATE_ERROR;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.NO_DATA_FOUND;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.TOO_MANY_REQUESTS;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.UNAUTHORIZED_ERROR;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.SqlContants.GET_DISTINCT_SIDAM_ID;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.SqlContants.UPDATE_JOB_SQL;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.judicialapi.elinks.controller.request.RefreshRoleRequest;
import uk.gov.hmcts.reform.judicialapi.elinks.exception.ElinksException;
import uk.gov.hmcts.reform.judicialapi.elinks.response.SchedulerJobStatusResponse;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ELinksService;
import uk.gov.hmcts.reform.judicialapi.elinks.service.ElinksPeopleService;
import uk.gov.hmcts.reform.judicialapi.elinks.service.IdamElasticSearchService;
import uk.gov.hmcts.reform.judicialapi.elinks.service.PublishSidamIdService;
import uk.gov.hmcts.reform.judicialapi.elinks.servicebus.ElinkTopicPublisher;
import uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataConstants;
import uk.gov.hmcts.reform.judicialapi.versions.V2;

@RestController
@RequestMapping(
        path = "/refdata/internal/topicPublish"
)
@Slf4j
@AllArgsConstructor
@Hidden
@ConditionalOnProperty(name = "testing.support.enabled", havingValue = "true")
@SuppressWarnings("all")
public class TestTopicPublishController {

    @Autowired
    ELinksService eLinksService;

    @Autowired
    ElinksPeopleService elinksPeopleService;

    @Autowired
    IdamElasticSearchService idamElasticSearchService;

    @Autowired
    PublishSidamIdService publishSidamIdService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ElinkTopicPublisher elinkTopicPublisher;


    @ApiResponse(responseCode = "200", description = "Publish all SIDAM id's to ASB")
    @ApiResponse(responseCode = "400", description = BAD_REQUEST)
    @ApiResponse(responseCode = "401", description = UNAUTHORIZED_ERROR)
    @ApiResponse(responseCode = "403", description = FORBIDDEN_ERROR)
    @ApiResponse(responseCode = "404", description = NO_DATA_FOUND)
    @ApiResponse(responseCode = "429", description = TOO_MANY_REQUESTS)
    @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    @GetMapping(path = "/publish",
        produces = V2.MediaType.SERVICE)
    public ResponseEntity<SchedulerJobStatusResponse> publishSidamIdToAsb() {
        try {
        // Get all sidam id's from the judicial_user_profile table
        List<String> sidamIds1 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds2 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds3 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds4 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds5 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds6 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds7 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds8 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds9 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds10 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds11 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds12 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds13 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds14 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);
            List<String> sidamIds15 = jdbcTemplate.query(GET_DISTINCT_SIDAM_ID, RefDataConstants.ROW_MAPPER);

        List<String> sidamIds = new ArrayList<>();
        for(int i =0 ; i<300 ; i++){
            sidamIds.add(sidamIds1.get(i));
            sidamIds.add(sidamIds2.get(i));
            sidamIds.add(sidamIds3.get(i));
            sidamIds.add(sidamIds4.get(i));
            sidamIds.add(sidamIds5.get(i));
            sidamIds.add(sidamIds6.get(i));
            sidamIds.add(sidamIds7.get(i));
            sidamIds.add(sidamIds8.get(i));
            sidamIds.add(sidamIds9.get(i));
            sidamIds.add(sidamIds10.get(i));
            sidamIds.add(sidamIds11.get(i));
            sidamIds.add(sidamIds12.get(i));
            sidamIds.add(sidamIds13.get(i));
            sidamIds.add(sidamIds14.get(i));
            sidamIds.add(sidamIds15.get(i));
        }

        sidamIds =
            sidamIds.stream()
                .map(String::strip)
                .filter(s -> !s.isBlank())
                .toList();

        Integer sidamIdcount = sidamIds.size();
            String jobId = "1234";
        elinkTopicPublisher.sendMessage(sidamIds, jobId);

        int i = jdbcTemplate.update(UPDATE_JOB_SQL, "SUCCESS", Integer.valueOf(jobId));

        SchedulerJobStatusResponse response = SchedulerJobStatusResponse.builder().id(jobId)
            .jobStatus("SUCCESS").sidamIdsCount(sidamIdcount).statusCode(HttpStatus.OK.value()).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }catch (Exception ex) {
            throw new ElinksException(HttpStatus.FORBIDDEN, JOB_DETAILS_UPDATE_ERROR, JOB_DETAILS_UPDATE_ERROR);
        }
    }


    @ApiResponse(responseCode = "200", description = "Publish all SIDAM id's to ASB")
    @ApiResponse(responseCode = "400", description = BAD_REQUEST)
    @ApiResponse(responseCode = "401", description = UNAUTHORIZED_ERROR)
    @ApiResponse(responseCode = "403", description = FORBIDDEN_ERROR)
    @ApiResponse(responseCode = "404", description = NO_DATA_FOUND)
    @ApiResponse(responseCode = "429", description = TOO_MANY_REQUESTS)
    @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    @PostMapping(path = "/publish",
        produces = V2.MediaType.SERVICE)
    public ResponseEntity<SchedulerJobStatusResponse> publishSidamIdToAsbHardCodedIds(
        @RequestBody RefreshRoleRequest refreshRoleRequest) {
        try {

            String jobId = "1234";
            elinkTopicPublisher.sendMessage(refreshRoleRequest.getSidamIds(), jobId);

            SchedulerJobStatusResponse response = SchedulerJobStatusResponse.builder().id(jobId)
                .jobStatus("SUCCESS").sidamIdsCount(refreshRoleRequest.getSidamIds().size())
                .statusCode(HttpStatus.OK.value()).build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception ex) {
            throw new ElinksException(HttpStatus.FORBIDDEN, JOB_DETAILS_UPDATE_ERROR, JOB_DETAILS_UPDATE_ERROR);
        }
    }
}
