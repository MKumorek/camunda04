package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.dmn.DecisionEvaluationBuilder;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import static java.util.Collections.singletonMap;

@RestController
public class DecisionController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private DecisionService decisionService;

    @RequestMapping("/load/{fileName}")
    public void hello(@PathVariable("fileName") String fileName) throws FileNotFoundException {

        InputStream stream = new FileInputStream("C:/dev/" + fileName);

        repositoryService
                .createDeployment()
                .addInputStream(fileName, stream)
                .deploy();
    }

    @RequestMapping("/run/{decision}/{temp}")
    public String run(@PathVariable("decision") String decision, @PathVariable("temp") String temp) {

        DecisionEvaluationBuilder check = decisionService.evaluateDecisionTableByKey(decision);

        check.variables(singletonMap("temperature", temp));

        Map<String, Object> entryMap = check
                .evaluate()
                .getFirstResult()
                .getEntryMap();

        return "result: " + entryMap;
    }

}
