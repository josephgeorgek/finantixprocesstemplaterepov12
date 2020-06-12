package com.finantix.service.extensions;

import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Group;
import org.kie.api.task.model.I18NText;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.User;
import org.kie.internal.runtime.manager.InternalRuntimeEngine;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.kie.internal.task.api.InternalTaskService;
import org.kie.internal.task.api.TaskModelProvider;
import org.kie.internal.task.api.model.InternalPeopleAssignments;
import org.kie.internal.task.api.model.InternalTask;
import org.kie.internal.task.api.model.InternalTaskData;
import org.kie.internal.task.api.prediction.PredictionOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finantix.mdl.adbk.core.dto.AddressDTO;
import com.finantix.mdl.adbk.core.dto.AddressesDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jbpm.services.task.exception.PermissionDeniedException;
import org.jbpm.services.task.impl.model.I18NTextImpl;
import org.jbpm.services.task.impl.model.PeopleAssignmentsImpl;
import org.jbpm.services.task.impl.model.TaskImpl;
import org.jbpm.services.task.impl.model.UserImpl;
import org.jbpm.services.task.utils.TaskFluent;
import org.jbpm.services.task.wih.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.drools.core.process.instance.impl.WorkItemImpl;
import org.jbpm.process.workitem.rest.RESTServiceException;
import org.jbpm.process.workitem.rest.RESTWorkItemHandler;
import org.drools.core.util.StringUtils;
import org.kie.internal.task.api.model.InternalOrganizationalEntity;


public class ChangeAddressForm extends LocalHTWorkItemHandler {
	   private static final Logger logger = LoggerFactory.getLogger(ChangeAddressForm.class);
	public ChangeAddressForm(RuntimeManager runtimeManager) {
		super.setRuntimeManager(runtimeManager);
	
	}
	
	@Override
	protected Task createTaskBasedOnWorkItemParams(KieSession session, WorkItem workItem) {
		System.out.println("");
		System.out.println("ChangeAddressForm createTaskBasedOnWorkItemParams");
		System.out.println("");
		InternalTask task = (InternalTask) super.createTaskBasedOnWorkItemParams(session, workItem);
		
	//	task.setId(323223l);
		task.setName("ChangeAddressForm");;
		task.setDescription("this is about ChangeAddressForm");
		task.setFormName("323223");
		
		Group HR = TaskModelProvider.getFactory().newGroup();
		((InternalOrganizationalEntity) HR).setId("HR");
		
		User admin = TaskModelProvider.getFactory().newUser();
		((InternalOrganizationalEntity) admin).setId("wbadmin");
		
		task.getPeopleAssignments().getBusinessAdministrators().add(HR);
		task.getPeopleAssignments().getPotentialOwners().add(HR);
		((InternalTaskData) task.getTaskData()).setActualOwner(new UserImpl("wbadmin"));
    	
    //	((InternalTaskService) runtime.getTaskService()).claim(task.getId(), "wbadmin");
		 System.out.println("ChangeAddressForm createTaskBasedOnWorkItemParams task"+  task);
		 System.out.println("ChangeAddressForm createTaskBasedOnWorkItemParams getTaskData"+  (InternalTaskData) task.getTaskData());
		return task;
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		
		Properties prop = new Properties();
		String propFileName = "config.properties";

		System.out.println("");
		System.out.println("ChangeAddressForm executeWorkItem v1.3");
		System.out.println("");
	
		
	    RuntimeEngine runtime = getRuntimeManager().getRuntimeEngine(ProcessInstanceIdContext.get(workItem.getProcessInstanceId()));
        Task task = runtime.getTaskService().getTaskByWorkItemId(workItem.getId());
       
        KieSession ksessionById = ((InternalRuntimeEngine) runtime).internalGetKieSession();
       
    	System.out.println("executeWorkItem ksessionById : " + ksessionById);
      
		System.out.println("");
        System.out.println("ChangeAddressForm executeWorkItem"+  task);
        if (task != null) {
		System.out.println("ChangeAddressForm getPeopleAssignments"+   task.getPeopleAssignments());
		System.out.println("ChangeAddressForm getFormName"+   task.getFormName());
		System.out.println("ChangeAddressForm getTaskType"+   task.getTaskType());
		System.out.println("ChangeAddressForm getActualOwner"+   task.getTaskData());
		System.out.println("ChangeAddressForm getActualOwner"+   task.getTaskData().getActualOwner());
        }
        else {
        	
        	 task =  createTaskBasedOnWorkItemParams( ksessionById,  workItem);
        	 
		System.out.println("task:"+task);
		
		System.out.println("manager:"+manager);
		System.out.println("workItem:"+workItem);
		System.out.println("workItem getProcessInstanceId:"+workItem.getProcessInstanceId());
		System.out.println("getRuntimeManager:"+getRuntimeManager());
		//RuntimeManager manager2 = getManager(workItem);
    	manager.registerWorkItemHandler("ChangeAddressForm Human Task", this);
    	 String taskName = (String) workItem.getParameter("NodeName");
    	 System.out.println("taskName:"+taskName);
    	 String name =  workItem.getName();
    	 System.out.println("name:"+name);
    	
		//WorkItemImpl customworkItem = new WorkItemImpl();
		//customworkItem.setId(workItem.getId());
	///	customworkItem.setName(workItem.getName());
	//	customworkItem.setNodeId(workItem.getNodeId());
		
		Map<String, Object> orignalparams = workItem.getParameters();
		System.out.println("executeWorkItem orignalparams : " + orignalparams);
		
		orignalparams.put("uploadFormParam", "uploadForm");
		orignalparams.put("uploadFormParam1", "uploadForm2");
			
		((WorkItemImpl) workItem).setParameters(orignalparams);
		System.out.println("executeWorkItem orignalparams : " + workItem.getParameters());
/*
 * 
		//assignments.setPotentialOwners(potentialOwners);
		List<OrganizationalEntity> businessAdministrators = new ArrayList<OrganizationalEntity>();
		businessAdministrators.add(new UserImpl("wbadmin"));
		InternalPeopleAssignments assignments;
		assignments.setBusinessAdministrators(businessAdministrators);
		task.setPeopleAssignments(assignments);
		
		 System.out.println("executeWorkItem getTaskData : " + task.getTaskData());
		((InternalTaskData) task.getTaskData()).setActualOwner(new UserImpl("wbadmin"));
		  
		   ((InternalTaskService) runtime.getTaskService()).addTask(task, content);
		// RuntimeEngine runtime = runtimeManager.getRuntimeEngine(ProcessInstanceIdContext.get(workItem.getProcessInstanceId()));
	       */
		 Map<String, Object> content = createTaskDataBasedOnWorkItemParams(ksessionById, workItem);
		   System.out.println("executeWorkItem content : " + content);
	      /*  Task newetask = createTaskBasedOnWorkItemParams(ksessionById, workItem);
	        System.out.println("executeWorkItem newetask : " + newetask);
	        System.out.println("executeWorkItem newetask : " + newetask.getId());
	     */   
	       String formName = (String) workItem.getParameter("TaskName"); 
	  	 if(formName == null){
		formName = "UpdateClient.changeou-taskform" ;
	  	 }
		   // ((InternalTask) newetask).setFormName(formName);
		     ((InternalTask) task).setFormName(formName);
		      
			
		        
	       
	        System.out.println("executeWorkItem getTaskData : " + task.getTaskData());
	    	((InternalTaskData) task.getTaskData()).setActualOwner(new UserImpl("wbadmin"));
	    	
	    	List<OrganizationalEntity> businessAdministrators = new ArrayList<OrganizationalEntity>();
			businessAdministrators.add(new UserImpl("wbadmin"));
			
			List<OrganizationalEntity> users = new ArrayList<OrganizationalEntity>();
			users.add(new UserImpl("wbadmin"));
			
			User admin = TaskModelProvider.getFactory().newUser();
			((InternalOrganizationalEntity) admin).setId("wbadmin");
			
			
			InternalPeopleAssignments assignments = new PeopleAssignmentsImpl();;
			assignments.setBusinessAdministrators(businessAdministrators);
			assignments.setPotentialOwners(users);
		
			((InternalTask) task).setPeopleAssignments(assignments);
		
		
			
		        
		        
		        
	    //	((InternalTaskService) runtime.getTaskService()).claim(newetask.getId(), "wbadmin");
	    	
			// System.out.println("before executeWorkItem : adding NonManagedLocalHTWorkItemHandler");
				
			//	ksessionById.getWorkItemManager().registerWorkItemHandler("Human Task",
				//    		new NonManagedLocalHTWorkItemHandler(ksessionById, runtime.getTaskService()));
	    	
		super.executeWorkItem(workItem, manager);
		
		
		
		// System.out.println("executeWorkItem executeWorkItem called : adding NonManagedLocalHTWorkItemHandler");
		
		//ksessionById.getWorkItemManager().registerWorkItemHandler("Human Task",
		 //   		new NonManagedLocalHTWorkItemHandler(ksessionById, runtime.getTaskService()));
		 System.out.println("executeWorkItem executeWorkItem called : getTaskData "+task.getTaskData());
		 String deploymentId = "UpdateClient_1.0.0-SNAPSHOT";
		 if (task.getTaskData() != null) {
		  deploymentId =  task.getTaskData().getDeploymentId();
		  System.out.println("executeWorkItem executeWorkItem called : deploymentId "+deploymentId);
		 }
		 System.out.println("executeWorkItem executeWorkItem called : deploymentId "+deploymentId);
		 System.out.println("executeWorkItem executeWorkItem called : now TaskFluent ");
		 
		
		
		Task mytask = new TaskFluent().setName("Additional Documents required")
	                .addPotentialUser("wbadmin")
	                .addPotentialGroup("HR")
	                .setAdminUser("wbadmin")
	                .setProcessId("UpdateClient.changeou")
	                .setFormName(formName)
	                .setWorkItemId(workItem.getId())
	                .setProcessSessionId(ksessionById.getId())
	                .setProcessInstanceId(workItem.getProcessInstanceId())
	                .setDeploymentID(deploymentId)
	                .getTask();
	
		 ((InternalTaskService) runtime.getTaskService()).addTask(mytask, workItem.getParameters());
		 System.out.println("executeWorkItem added TaskFluent "+mytask);

      //  Map<String, Object> content = createTaskDataBasedOnWorkItemParams(ksessionById, workItem);
      /*  System.out.println("executeWorkItem new Task content : "+content);
        Task task2 = createTaskBasedOnWorkItemParams(ksessionById, workItem);
        
         System.out.println("executeWorkItem createTaskDataBasedOnWorkItemParams new task : "+task2.getId());
        long taskId = createTaskInstance((InternalTaskService) runtime.getTaskService(), task2, workItem, ksessionById, content);
        System.out.println("executeWorkItem createTaskDataBasedOnWorkItemParams new task  taskId: "+task2);
        ((InternalTaskService) runtime.getTaskService()).setOutput(taskId, ADMIN_USER, content);
        System.out.println("executeWorkItem createTaskDataBasedOnWorkItemParams new task  taskId getTaskService: "+((InternalTaskService) runtime.getTaskService()).getTaskById(taskId));
        ((InternalTaskService) runtime.getTaskService()).addTask(task2, content);*/
        
        // manager.completeWorkItem(workItem.getId(), outcome.getData());
	//	 Task task = createTaskBasedOnWorkItemParams(ksessionById, workItem);
      //  System.out.println("executeWorkItem calling completeWorkItem ");
		//manager.completeWorkItem(workItem.getId(), content);
		//  System.out.println("executeWorkItem calling completeWorkItem ");
        }
        }

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		super.abortWorkItem(workItem, manager);
	}

	

	

}
