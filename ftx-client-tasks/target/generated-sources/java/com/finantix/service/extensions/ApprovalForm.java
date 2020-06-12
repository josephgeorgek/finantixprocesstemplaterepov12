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
import org.kie.api.task.model.PeopleAssignments;
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
import org.jbpm.services.task.impl.model.GroupImpl;
import org.jbpm.services.task.impl.model.I18NTextImpl;
import org.jbpm.services.task.impl.model.PeopleAssignmentsImpl;
import org.jbpm.services.task.impl.model.TaskImpl;
import org.jbpm.services.task.impl.model.UserImpl;
import org.jbpm.services.task.utils.TaskFluent;
import org.jbpm.services.task.wih.*;
import org.jbpm.services.task.wih.util.PeopleAssignmentHelper;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
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


public class ApprovalForm extends LocalHTWorkItemHandler {
	
	String approvers_Group = null;
	   private static final Logger logger = LoggerFactory.getLogger(ApprovalForm.class);
	public ApprovalForm(RuntimeManager runtimeManager) {
		super.setRuntimeManager(runtimeManager);
	
	}
	
	@Override
	protected Task createTaskBasedOnWorkItemParams(KieSession session, WorkItem workItem) {
		System.out.println("");
		System.out.println("ApprovalForm createTaskBasedOnWorkItemParams");
		System.out.println("");
		InternalTask task = (InternalTask) super.createTaskBasedOnWorkItemParams(session, workItem);
		
		//	task.setId(323223l);
			task.setName("ChangeAddressForm");;
			task.setDescription("this is about ChangeAddressForm");
			task.setFormName("323223");
			
			
			Group HR = TaskModelProvider.getFactory().newGroup();
			((InternalOrganizationalEntity) HR).setId(approvers_Group);
			
			User admin = TaskModelProvider.getFactory().newUser();
			((InternalOrganizationalEntity) admin).setId("wbadmin");
			
			task.getPeopleAssignments().getBusinessAdministrators().add(HR);
			task.getPeopleAssignments().getPotentialOwners().add(HR);
		//	((InternalTaskData) task.getTaskData()).setActualOwner(new UserImpl("wbadmin"));
			
	    //	((InternalTaskService) runtime.getTaskService()).claim(task.getId(), "wbadmin");
			 System.out.println("ApprovalForm createTaskBasedOnWorkItemParams task"+  task);
			 System.out.println("task getPotentialOwners:" + task.getPeopleAssignments().getPotentialOwners());
			 System.out.println("ApprovalForm createTaskBasedOnWorkItemParams getTaskData"+  (InternalTaskData) task.getTaskData());
	return task;
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

		Properties prop = new Properties();
		String propFileName = "config.properties";

		System.out.println("");
		System.out.println("ApprovalForm executeWorkItem v1.6");
		System.out.println("");

		RuntimeEngine runtime = getRuntimeManager()
				.getRuntimeEngine(ProcessInstanceIdContext.get(workItem.getProcessInstanceId()));

		KieSession ksessionById = ((InternalRuntimeEngine) runtime).internalGetKieSession();
		org.jbpm.process.instance.ProcessInstance processInstance = (org.jbpm.process.instance.ProcessInstance) ksessionById
				.getProcessInstance(workItem.getProcessInstanceId());

		 approvers_Group = (String) ((WorkflowProcessInstance) processInstance).getVariable("approvers_Group");

			
			
		Task task = createTaskBasedOnWorkItemParams(ksessionById, workItem);
		System.out.println("task:" + task + "approvers_Group:"+approvers_Group);
		PeopleAssignmentHelper peopleAssignmentHelper = new PeopleAssignmentHelper();
	
		
		peopleAssignmentHelper.handlePeopleAssignments(workItem, (InternalTask)task, (InternalTaskData) task.getTaskData() );

		
		
		System.out.println("task:" + task + "approvers_Group:"+approvers_Group);

		System.out.println("executeWorkItem getTaskData : " + task.getTaskData());
		/*
		System.out.println("task getPotentialOwners:" + ((InternalTask)task.getPeopleAssignments()).getPotentialOwners());
		((InternalTaskData) task.getTaskData()).setActualOwner(new UserImpl("wbadmin"));
		
		Group PM = TaskModelProvider.getFactory().newGroup();
		((InternalOrganizationalEntity) PM).setId("PM");
		
		Group approvers = TaskModelProvider.getFactory().newGroup();
		((InternalOrganizationalEntity) approvers).setId(approvers_Group);
		
		User admin = TaskModelProvider.getFactory().newUser();
		((InternalOrganizationalEntity) admin).setId("wbadmin");
		
		task.getPeopleAssignments().getBusinessAdministrators().add(PM);
		task.getPeopleAssignments().getPotentialOwners().add(PM);
		task.getPeopleAssignments().getPotentialOwners().add(approvers);
		((InternalTaskData) task.getTaskData()).setActualOwner(new UserImpl("wbadmin"));
    	
		System.out.println("task getPeopleAssignments:" + task.getPeopleAssignments());
		System.out.println("task getPotentialOwners:" + task.getPeopleAssignments().getPotentialOwners());*/
	/*	System.out.println("executeWorkItem getTaskData : " + task.getTaskData());
		((InternalTaskData) task.getTaskData()).setActualOwner(new UserImpl("wbadmin"));

		List<OrganizationalEntity> businessAdministrators = new ArrayList<OrganizationalEntity>();
		businessAdministrators.add(new UserImpl("wbadmin"));

		List<OrganizationalEntity> users = new ArrayList<OrganizationalEntity>();
		users.add(new UserImpl("wbadmin"));
		users.add(new GroupImpl(approvers_Group));

	//	User admin = TaskModelProvider.getFactory().newUser();
	//	((InternalOrganizationalEntity) admin).setId("wbadmin");

		InternalPeopleAssignments assignments = new PeopleAssignmentsImpl();
	
		assignments.setBusinessAdministrators(businessAdministrators);
		assignments.setPotentialOwners(users);

		((InternalTask) task).setPeopleAssignments(assignments);
*/
		super.executeWorkItem(workItem, manager);
		System.out.println("executeWorkItem DONE");

	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		super.abortWorkItem(workItem, manager);
	}

	

	

}
