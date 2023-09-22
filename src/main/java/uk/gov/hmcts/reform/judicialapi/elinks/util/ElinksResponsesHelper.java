package uk.gov.hmcts.reform.judicialapi.elinks.util;


import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import uk.gov.hmcts.reform.judicialapi.elinks.domain.ElinksResponses;
import uk.gov.hmcts.reform.judicialapi.elinks.repository.ElinksResponsesRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


@Component
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ElinksResponsesHelper {

    @Value("${loggingComponentName}")
    private String loggingComponentName;

    @Autowired
    private ElinksResponsesRepository elinksResponsesRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveElinksResponse(String apiName, Response.Body elinksData) {

        ElinksResponses elinksResponses = new ElinksResponses();
        elinksResponses.setApiName(apiName);
        elinksResponses.setCreatedDate(LocalDateTime.now());


        try {
            elinksResponses.setElinksData(StreamUtils.copyToString(elinksData.asInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("hi");
        }
        elinksResponsesRepository.save(elinksResponses);
    }
}

