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
    public String load(@PathVariable("drgFileName") String drgFileName) throws IOException {
        
//        String resourcePath = "classpath:dmns/" + drgFileName;
//        InputStream drgStream = resourceLoader.getResource(resourcePath).getInputStream();
//
        InputStream drgStream = new FileInputStream("src/main/dmns/" + drgFileName);

        Deployment deploy = repositoryService
                .createDeployment()
                .addInputStream(drgFileName, drgStream)
//                .addClasspathResource(drgFileName)
                .deploy();

        return extractVersion((DeploymentEntity) deploy);
    }


    @RequestMapping("/run/{decision}/{temp}")
    public String run(@PathVariable("decision") String decision,
                      @PathVariable("temp") String temp) {

        return evaluate(decision, temp).toString();
    }

    @RequestMapping("/run/{decision}/{version}/{temp}")
    public String run(@PathVariable("decision") String decision,
                      @PathVariable("version") int version,
                      @PathVariable("temp") String temp) {

        return evaluate(decision, temp, version).toString();
    }

    private String extractVersion(DeploymentEntity deploy) {

        List<DecisionDefinitionEntity> dde = deploy
                .getDeployedArtifacts()
                .get(DecisionDefinitionEntity.class);
        return Integer.toString(dde.stream()
                .filter(d -> !d.getKey().equals("SUB") )
                .findFirst()
                .get()
                .getVersion());
    }

    private Map<String, Object> evaluate(String decision, String temp) {
        return evaluate(decision, temp, null);
    }

    private Map<String, Object> evaluate(String decision,
                                         String temp,
                                         Integer version) {

        DecisionEvaluationBuilder check = decisionService
                .evaluateDecisionTableByKey(decision)
                .version(version);

        HashMap<String, Object> map = new HashMap<>();
        map.put("temperature", temp);

        check.variables(map);

        return check
                .evaluate()
                .getFirstResult()
                .getEntryMap();
    }

}
