package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Date;

public class SendInformation implements JavaDelegate {

    public void execute(DelegateExecution delegateExecution) {

        System.out.println(new Date() + "You didn't process the number, so let's finish");
        System.out.println(delegateExecution.getVariables());

    }

}
