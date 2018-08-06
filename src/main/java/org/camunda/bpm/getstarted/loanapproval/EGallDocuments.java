package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.model.bpmn.impl.instance.ExclusiveGatewayImpl;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;

public class EGallDocuments extends ExclusiveGatewayImpl {


    public EGallDocuments(ModelTypeInstanceContext context) {
        super(context);
    }

    public void start(){
        System.out.println("we are in EA all Documents");
    }
}
