package uk.gov.hmcts.reform.judicialapi.elinks.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.AUTHORIZATION;

@Component
public class TestElinksImpl {

    @Autowired
    RestTemplate restTemplate;

    @Value("${elinksUrl}")
    String elinksUrl;

    @Value("${elinksApiKey}")
    private String elinksApiKey;


    public ResponseEntity<Object> mockService(String path) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AUTHORIZATION, "Token " + elinksApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.getForEntity(elinksUrl + "/" + path, Object.class,entity);
    }
}
