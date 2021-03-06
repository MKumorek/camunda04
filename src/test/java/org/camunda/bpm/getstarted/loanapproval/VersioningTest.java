package org.camunda.bpm.getstarted.loanapproval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VersioningTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testDecisionCel() {
        this.restTemplate.getForObject("/load/cel.dmn", String.class);
        
        String response = this.restTemplate.getForObject("/run/CEL/10", String.class);
        
        assertEquals("{form=water}", response);
    }
    
    @Test
    public void testDecisionFar() {
        this.restTemplate.getForObject("/load/far.dmn", String.class);
        
        String response = this.restTemplate.getForObject("/run/FAR/10", String.class);
        
        assertEquals("{form=ice}", response);
    }
    
    @Test
    public void testSubdecisionCel() {
        this.restTemplate.getForObject("/load/cel.dmn", String.class);
        
        String response = this.restTemplate.getForObject("/run/SUB/10", String.class);
        
        assertEquals("{warm=true}", response);
        
    }
    
    @Test
    public void testSubdecisionFar() {
        this.restTemplate.getForObject("/load/far.dmn", String.class);
        
        String response = this.restTemplate.getForObject("/run/SUB/10", String.class);
        
        assertEquals("{warm=false}", response);
        
    }
    
    @Test
    public void testIndependence() {
        this.restTemplate.getForObject("/load/far.dmn", String.class);
        this.restTemplate.getForObject("/load/cel.dmn", String.class);
        
        String response = this.restTemplate.getForObject("/run/CEL/-1", String.class);
        assertEquals("{form=ice}", response);
        
        response = this.restTemplate.getForObject("/run/CEL/1", String.class);
        assertEquals("{form=water}", response);
        
        response = this.restTemplate.getForObject("/run/FAR/-1", String.class);
        assertEquals("{form=ice}", response);
        
        response = this.restTemplate.getForObject("/run/FAR/1", String.class);
        assertEquals("{form=ice}", response);
        
        response = this.restTemplate.getForObject("/run/SUB/1", String.class);
        assertEquals("{warm=true}", response);
        
    }
    
}
