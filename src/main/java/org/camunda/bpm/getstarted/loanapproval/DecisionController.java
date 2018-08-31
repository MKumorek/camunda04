package org.camunda.bpm.getstarted.loanapproval;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.dmn.DecisionEvaluationBuilder;
import org.camunda.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DecisionController {
    
    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private DecisionService decisionService;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @RequestMapping("/load/{drgFileName}")
    public void load(@PathVariable("drgFileName") String drgFileName) throws IOException {
        
//        String resourcePath = "classpath:dmns/" + drgFileName;
//        InputStream drgStream = resourceLoader.getResource(resourcePath).getInputStream();
//
        InputStream drgStream = new FileInputStream("src/main/dmns/" + drgFileName);

        repositoryService
                .createDeployment()
                .addInputStream(drgFileName, drgStream)
//                .addClasspathResource(drgFileName)
                .deploy();
    }

    @RequestMapping("/run/{decision}/{temp}")
    public String run(@PathVariable("decision") String decision, @PathVariable("temp") String temp) {
        
        DecisionEvaluationBuilder check = decisionService.evaluateDecisionTableByKey(decision);
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("temperature", temp);
        
        check.variables(map);
        
        Map<String, Object> entryMap = check
                .evaluate()
                .getFirstResult()
                .getEntryMap();
        
        return "result: " + entryMap;
    }
    
}
