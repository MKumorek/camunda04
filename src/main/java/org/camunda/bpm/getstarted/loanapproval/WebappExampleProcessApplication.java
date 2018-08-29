package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.dmn.DecisionEvaluationBuilder;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import static java.util.Collections.singletonMap;

@SpringBootApplication
@EnableProcessApplication
public class WebappExampleProcessApplication {

	@Autowired
	private DecisionService decisionService;

	public static void main(String... args) {
		SpringApplication.run(WebappExampleProcessApplication.class, args);
	}
	
	@EventListener
	private void processPostDeploy(PostDeployEvent event) {
//		runtimeService.startProcessInstanceByKey("loanApproval");
//		runtimeService.startProcessInstanceByKey("numbers_bound");
		runSampleDmn();
	}

	private void runSampleDmn() {
		DecisionEvaluationBuilder checkIfLow = decisionService.evaluateDecisionTableByKey("checkIfLow");

		checkIfLow
				.variables(singletonMap("number", 13));

		Map<String, Object> entryMap = checkIfLow
				.evaluate()
				.getFirstResult()
				.getEntryMap();

		System.out.println("result: " + entryMap);
	}
}