/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.customer.services;

import java.math.BigInteger;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

/**
 *
 * @author salaboy
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import org.jbpm.process.workitem.core.AbstractLogOrThrowWorkItemHandler;
import org.jbpm.process.workitem.core.util.RequiredParameterValidator;
import org.jbpm.process.workitem.core.util.Wid;
import org.jbpm.process.workitem.core.util.WidMavenDepends;
import org.jbpm.process.workitem.core.util.WidParameter;
import org.jbpm.process.workitem.core.util.service.WidAction;
import org.jbpm.process.workitem.core.util.service.WidService;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

@Wid(widfile = "RSSDefinitions.wid", name = "RSS",
        displayName = "RSS",
        defaultHandler = "mvel: new org.jbpm.process.workitem.rss.RSSWorkItemHandler()",
        documentation = "${artifactId}/index.html",
        category = "${artifactId}",
        icon = "RSS.png",
        parameters = {
                @WidParameter(name = "URL", required = true)
        },
        mavenDepends = {
                @WidMavenDepends(group = "${groupId}", artifact = "${artifactId}", version = "${version}")
        },
        serviceInfo = @WidService(category = "${name}", description = "${description}",
                keywords = "rss,feed,create",
                action = @WidAction(title = "Create a RSS feed from multiple sources")
        ))
public class CreateCustomerWorkItemHandler implements WorkItemHandler{

    public CreateCustomerWorkItemHandler() {
    }

    
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        
   System.out.println("executeWorkItem ");
   System.out.println("executeWorkItem CreateCustomerWorkItemHandler");
       
        manager.completeWorkItem(workItem.getId(), null);
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
