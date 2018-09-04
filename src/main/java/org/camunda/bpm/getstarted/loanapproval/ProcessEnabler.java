package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.dmn.DecisionEvaluationBuilder;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Collections;
import java.util.Map;

@Configuration
@EnableProcessApplication
public class ProcessEnabler {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private DecisionService decisionService;

    @EventListener
    private void processPostDeploy(PostDeployEvent event) {
        // runtimeService.startProcessInstanceByKey("loanApproval");
        loadAndRunProcess();
        // runtimeService.startProcessInstanceByKey("numbers_bound");
        // runSampleDmn();
    }

    private void loadAndRunProcess() {
        repositoryService
                .createDeployment()
                .addClasspathResource("numbers.bpmn")
                .addClasspathResource("checkIfLow.dmn")
                .deploy();
        runtimeService.startProcessInstanceByKey("numbers");
    }

    private void runSampleDmn() {
        DecisionEvaluationBuilder checkIfLow = decisionService.evaluateDecisionTableByKey("checkIfLow");

        checkIfLow
                .variables(Collections.<String, Object> singletonMap("number", 13));

        Map<String, Object> entryMap = checkIfLow
                .evaluate()
                .getFirstResult()
                .getEntryMap();

        System.out.println("result: " + entryMap);
    }
}
