package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.dmn.DecisionEvaluationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DecisionController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private DecisionService decisionService;

    @RequestMapping("/load/{drgFileName}")
    public void hello(@PathVariable("drgFileName") String drgFileName) throws FileNotFoundException {

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

//        DecisionEvaluationBuilder check = decisionService.evaluateDecisionTableByKey(decision);
//
//        check.variables(Collections.<String, Object>singletonMap("temperature", temp));
//
//        Map<String, Object> entryMap = check
//                .evaluate()
//                .getFirstResult()
//                .getEntryMap();

        return "result: " + entryMap;
    }

}
