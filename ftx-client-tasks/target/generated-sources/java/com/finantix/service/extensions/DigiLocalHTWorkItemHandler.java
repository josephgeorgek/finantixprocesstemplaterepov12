package com.finantix.service.extensions;



import org.drools.core.process.instance.impl.WorkItemImpl;
import org.jbpm.services.task.exception.PermissionDeniedException;
import org.jbpm.services.task.impl.model.I18NTextImpl;
import org.jbpm.services.task.impl.model.TaskDataImpl;
import org.jbpm.services.task.impl.model.TaskImpl;
import org.jbpm.services.task.impl.model.UserImpl;
import org.jbpm.services.task.wih.LocalHTWorkItemHandler;
import org.jbpm.services.task.wih.util.PeopleAssignmentHelper;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.I18NText;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.PeopleAssignments;
import org.kie.api.task.model.Task;
import org.kie.internal.runtime.manager.InternalRuntimeEngine;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.kie.internal.task.api.model.InternalTask;
import org.kie.internal.task.api.model.InternalTaskData;
import org.slf4j.Logger;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
public class DigiLocalHTWorkItemHandler extends LocalHTWorkItemHandler {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DigiLocalHTWorkItemHandler.class);


	public DigiLocalHTWorkItemHandler(RuntimeManager runtimeManager) {
		super.setRuntimeManager(runtimeManager);
	
	}
    
   
	 

    public class PeopleAssignmentContext {
        private Task task;
        private WorkItem workItem;
        private KieSession kieSession;

        public PeopleAssignmentContext(Task task, WorkItem workItem, KieSession kieSession) {
            this.task = task;
            this.workItem = workItem;
            this.kieSession = kieSession;
        }

        public Task getTask() {
            return task;
        }

        public WorkItem getWorkItem() {
            return workItem;
        }

        public KieSession getKieSession() {
            return kieSession;
        }
    }
    
    

    @Override
    protected Task createTaskBasedOnWorkItemParams(KieSession session, WorkItem workItem) {
    	
    	System.out.println("");
		System.out.println("JahiaLocalHTWorkItemHandler createTaskBasedOnWorkItemParams");
		System.out.println("");
		
        InternalTask task = new TaskImpl();
        String taskName = (String) workItem.getParameter("NodeName");
        if (taskName != null) {
            List<I18NText> names = new ArrayList<I18NText>();
            names.add(new I18NTextImpl("en", taskName));
            task.setNames(names);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Create workflow task [%s]", taskName));
        }
        
        System.out.println("");
		System.out.println("JahiaLocalHTWorkItemHandler Create workflow task"+taskName);
		System.out.println("");
//        // this should be replaced by FormName filled by designer
//        // TaskName shouldn't be trimmed if we are planning to use that for the task lists
//        String formName = (String) workItem.getParameter("TaskName");
//        if(formName != null){
//            task.setFormName(formName);
//        }

        String comment = (String) workItem.getParameter("Comment");
        if (comment == null) {
            comment = "";
        }
        
        System.out.println("");
     		System.out.println("JahiaLocalHTWorkItemHandler Create workflow task"+taskName);
     		System.out.println("");
     		
     		
        List<I18NText> descriptions = new ArrayList<I18NText>();
        descriptions.add(new I18NTextImpl("en", comment));
        task.setDescriptions(descriptions);
        List<I18NText> subjects = new ArrayList<I18NText>();
        subjects.add(new I18NTextImpl("en", comment));
        task.setSubjects(subjects);
        String priorityString = (String) workItem.getParameter("Priority");
        int priority = 0;
        if (priorityString != null) {
            try {
                priority = new Integer(priorityString);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        task.setPriority(priority);
        InternalTaskData taskData = new TaskDataImpl();
        taskData.setWorkItemId(workItem.getId());
        taskData.setProcessInstanceId(workItem.getProcessInstanceId());
        if (session.getProcessInstance(workItem.getProcessInstanceId()) != null) {
            taskData.setProcessId(session.getProcessInstance(workItem.getProcessInstanceId()).getProcess().getId());
            String deploymentId = (String) session.getEnvironment().get("deploymentId");
            taskData.setDeploymentId(deploymentId);
        }
        taskData.setProcessSessionId(session.getId());
        taskData.setSkipable(!"false".equals(workItem.getParameter("Skippable")));
        //Sub Task Data
        Long parentId = (Long) workItem.getParameter("ParentId");
        if (parentId != null) {
            taskData.setParentId(parentId);
        }
        String createdBy = (String) workItem.getParameter("CreatedBy");
        if (createdBy != null && createdBy.trim().length() > 0) {
            taskData.setCreatedBy(new UserImpl(createdBy));
        } else   {
            taskData.setCreatedBy(new UserImpl("wbadmin"));
        }
        taskData.setCreatedOn(new Date());
       /* WorkflowVariable dueDate = (WorkflowVariable) workItem.getParameter("dueDate");
        if (dueDate != null) {
            taskData.setExpirationTime(dueDate.getValueAsDate());
        }*/
        PeopleAssignmentHelper peopleAssignmentHelper = new PeopleAssignmentHelper();
        peopleAssignmentHelper.handlePeopleAssignments(workItem, task, taskData);

        PeopleAssignments peopleAssignments = task.getPeopleAssignments();
        List<OrganizationalEntity> businessAdministrators = peopleAssignments.getBusinessAdministrators();
       
        task.setTaskData(taskData);
   //     task.setDeadlines(HumanTaskHandlerHelper.setDeadlines(workItem, businessAdministrators, session.getEnvironment()));

        /*PeopleAssignmentContext peopleAssignmentContext = new PeopleAssignmentContext(task, workItem, session);
        try {
            peopleAssignmentPipeline.invoke(peopleAssignmentContext);
        } catch (PipelineException e) {
            logger.error(e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
        }*/

        return task;
    }

    @Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
    	

		System.out.println("");
		System.out.println("JahiaLocalHTWorkItemHandler executeWorkItem");
		System.out.println("");
		
		System.out.println("manager:"+manager);
		System.out.println("workItem:"+workItem);
		System.out.println("workItem getProcessInstanceId:"+workItem.getProcessInstanceId());
		System.out.println("getRuntimeManager:"+getRuntimeManager());
		//RuntimeManager manager2 = getManager(workItem);
    	manager.registerWorkItemHandler("Human Task", this);
    	 String taskName = (String) workItem.getParameter("NodeName");
    
    	//  RuntimeEngine runtime = getRuntimeManager().getRuntimeEngine(ProcessInstanceIdContext.get(workItem.getProcessInstanceId()));
    	  
    	//	System.out.println("runtime:"+runtime);
    	//super.setRuntimeManager(runtime);

		Properties prop = new Properties();
		String propFileName = "config.properties";

		System.out.println("");
		System.out.println("JahiaLocalHTWorkItemHandler executeWorkItem" +  taskName);
		System.out.println("");
		
		WorkItemImpl customworkItem = new WorkItemImpl();
		RuntimeEngine runtime = getRuntimeManager().getRuntimeEngine(ProcessInstanceIdContext.get(workItem.getProcessInstanceId()));
		Map<String, Object> orignalparams = workItem.getParameters();
		System.out.println("executeWorkItem JahiaLocalHTWorkItemHandler orignalparams : " + orignalparams);
		 KieSession ksessionById = ((InternalRuntimeEngine) runtime).internalGetKieSession();
	    	System.out.println("executeWorkItem JahiaLocalHTWorkItemHandler ksessionById : " + ksessionById);
	        Task newetask = createTaskBasedOnWorkItemParams(ksessionById, workItem);
	        System.out.println("executeWorkItem JahiaLocalHTWorkItemHandler newetask : " + newetask);
		super.executeWorkItem(customworkItem, manager);

		//manager.completeWorkItem(workItem.getId(), results);
	}

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
    	
    	System.out.println("");
 		System.out.println("JahiaLocalHTWorkItemHandler abortWorkItem");
 		System.out.println("JahiaLocalHTWorkItemHandler abortWorkItem"+workItem.getName());
 		System.out.println("");
 		
 		
 		
        RuntimeEngine runtime = getRuntimeManager().getRuntimeEngine(ProcessInstanceIdContext.get(workItem.getProcessInstanceId()));
        Task task = runtime.getTaskService().getTaskByWorkItemId(workItem.getId());
        if (task != null) {
            try {
                runtime.getTaskService().exit(task.getId(), "/users/root");
            } catch (PermissionDeniedException e) {
                logger.info(e.getMessage());
            }
        }
    }
}
