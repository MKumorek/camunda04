package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Random;

public class GenerateNumber implements JavaDelegate {

    Random rand = new Random();

    public void execute(DelegateExecution delegateExecution) throws Exception {

        int number = rand.nextInt(100);
        delegateExecution.setVariable("number", number);
        delegateExecution.setVariable("isEven", isEven(number));
        System.out.println("my number is: " + number);
        System.out.println(delegateExecution.getVariables());

    }

    private boolean isEven(int number) {
        return 0 == number % 2;
    }

}
