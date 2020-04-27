/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.customer.services;


import java.util.List;
import org.jbpm.process.workitem.core.AbstractLogOrThrowWorkItemHandler;
import org.jbpm.process.workitem.core.util.Wid;
import org.jbpm.process.workitem.core.util.WidMavenDepends;
import org.jbpm.process.workitem.core.util.WidParameter;
import org.jbpm.process.workitem.core.util.service.WidAction;
import org.jbpm.process.workitem.core.util.service.WidService;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

/**
 *
 * @author jgeorge
 */

@Wid(widfile = "AddCustomerCommentsWorkItemHandler.wid", name = "AddCustomerComments",
        displayName = "AddCustomerComments",
        defaultHandler = "mvel: new org.jbpm.customer.services.AddCustomerCommentsWorkItemHandler()",
        documentation = "${artifactId}/index.html",
        category = "${artifactId}",
        icon = "email.png",
        parameters = {
                @WidParameter(name = "URL", required = true)
        },
        mavenDepends = {
                @WidMavenDepends(group = "${groupId}", artifact = "${artifactId}", version = "${version}")
        },
        serviceInfo = @WidService(category = "${name}", description = "${description}",
                keywords = "rss,feed,create",
                action = @WidAction(title = " AddCustomerComments feed from multiple sources")
        ))
public class AddCustomerCommentsWorkItemHandler extends AbstractLogOrThrowWorkItemHandler{

   
    
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
