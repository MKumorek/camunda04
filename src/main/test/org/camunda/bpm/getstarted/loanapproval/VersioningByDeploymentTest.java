package org.camunda.bpm.getstarted.loanapproval;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VersioningByDeploymentTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testDecisionCel() {
        this.restTemplate.getForObject("/load/cel.dmn", String.class);

        String response = this.restTemplate.getForObject("/run/CEL/-1", String.class);
        assertEquals("{form=ice}", response);

        response = this.restTemplate.getForObject("/run/CEL/1", String.class);
        assertEquals("{form=water}", response);
    }
    
    @Test
    public void testDecisionCel10() {
        this.restTemplate.getForObject("/load/celTreshold10.dmn", String.class);
        
        String response = this.restTemplate.getForObject("/run/CEL/9", String.class);
        assertEquals("{form=ice}", response);

        response = this.restTemplate.getForObject("/run/CEL/11", String.class);
        assertEquals("{form=water}", response);
    }

    @Test
    public void testIndependence() {
        String vCel = this.restTemplate.getForObject("/load/cel.dmn", String.class);
        String vCel10 = this.restTemplate.getForObject("/load/celTreshold10.dmn", String.class);

        String response = this.restTemplate.getForObject("/run/CEL/" + vCel + "/-1", String.class);
        assertEquals("{form=ice}", response);

        response = this.restTemplate.getForObject("/run/CEL/" + vCel + "/1", String.class);
        assertEquals("{form=water}", response);

        response = this.restTemplate.getForObject("/run/CEL/" + vCel10 + "/9", String.class);
        assertEquals("{form=ice}", response);

        response = this.restTemplate.getForObject("/run/CEL/" + vCel10 + "/11", String.class);
        assertEquals("{form=water}", response);

    }
    
}
