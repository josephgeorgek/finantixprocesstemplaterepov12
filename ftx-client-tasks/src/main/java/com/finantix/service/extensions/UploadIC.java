package com.finantix.service.extensions;
import java.util.Date;

import org.jbpm.services.task.utils.OnErrorAction;
import org.jbpm.services.task.wih.NonManagedLocalHTWorkItemHandler;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.internal.task.api.InternalTaskService;
import org.kie.internal.task.api.model.ContentData;

public class UploadIC extends NonManagedLocalHTWorkItemHandler {


	
	 @Override
	    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		 System.out.println("UploadIC before executeWorkItem : adding NonManagedLocalHTWorkItemHandler");
		 super.executeWorkItem(workItem,  manager);
		 System.out.println("UploadIC after executeWorkItem : adding NonManagedLocalHTWorkItemHandler");
		 
	    }

}
