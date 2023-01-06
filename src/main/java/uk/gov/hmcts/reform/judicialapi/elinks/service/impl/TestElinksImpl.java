package uk.gov.hmcts.reform.judicialapi.elinks.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.AUTHORIZATION;

@Slf4j
@Component
public class TestElinksImpl {

    @Autowired
    RestTemplate restTemplate;

    @Value("${elinksUrl}")
    String elinksUrl;

    @Value("${elinksApiKey}")
    private String elinksApiKey;


    public ResponseEntity<Object> mockService(String path) throws UnsupportedEncodingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AUTHORIZATION, "Token " + elinksApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = elinksUrl + "/" + decode(path);
        log.debug("token{}", headers.get(AUTHORIZATION));
        log.debug("elinks url path {}", url);
        return restTemplate.exchange(url, HttpMethod.GET,entity,Object.class);
    }


    private String decode(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
    }
}
