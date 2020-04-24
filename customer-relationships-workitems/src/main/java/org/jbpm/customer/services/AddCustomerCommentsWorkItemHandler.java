/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.customer.services;


import java.util.List;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

/**
 *
 * @author salaboy
 */
public class AddCustomerCommentsWorkItemHandler implements WorkItemHandler{

    public AddCustomerCommentsWorkItemHandler() {
    }

    
    public void executeWorkItem(WorkItem wi, WorkItemManager wim) {
            
            String email = (String)wi.getParameter("in_customer_email");
           
            String comment = (String)wi.getParameter("in_customer_comment");
            if(comment != null){
                //CustomerRelationshipsService.getInstance().addComment(email, comment, null );
                System.out.println(" Customer Comment Added to our database: "+ email);
            }else{
                System.out.println(" There was an error trying to add the comment to our database");
            }
            wim.completeWorkItem(wi.getId(), null);
            
    }

    public void abortWorkItem(WorkItem wi, WorkItemManager wim) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
