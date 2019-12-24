package uk.gov.hmcts.reform.judicialapi.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SpringBootIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Test
    public void test() throws Exception{
        restTemplate.getForObject("/refdata/v1/judicial/roles", Object.class);
    }

}
