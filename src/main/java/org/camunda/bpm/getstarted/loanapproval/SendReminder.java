package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Date;
import java.util.Random;

public class SendReminder implements JavaDelegate {

    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println(new Date() +  "We kindly remind about the task of accepting non-low, non-even number");
        System.out.println(delegateExecution.getVariables());

    }

    private boolean isEven(int number) {
        return 0 == number % 2;
    }

}