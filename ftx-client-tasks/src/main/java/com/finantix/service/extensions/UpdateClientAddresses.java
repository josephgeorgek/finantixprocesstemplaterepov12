package com.finantix.service.extensions;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finantix.mdl.adbk.core.dto.AddressDTO;
import com.finantix.mdl.adbk.core.dto.AddressesDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.drools.core.process.instance.impl.WorkItemImpl;
import org.jbpm.process.workitem.rest.RESTServiceException;
import org.jbpm.process.workitem.rest.RESTWorkItemHandler;
import org.drools.core.util.StringUtils;

//com.finantix.service.extensions.FinantixServiceMEDocStatusUpdate
public class UpdateClientAddresses extends RESTWorkItemHandler {
	// http://prd-cm-dk-03.fx.lan:8080/rest/api/tenant/locales
	String serverURL = "/rest/adbk/contacts/"; ///rest/adbk/contacts/-1/addresses 
	String host = "";
	String User = "admin@thedigitalstack.com";
	String Password = "password";
	
	private static final String RESULTS_VALUE = "clientAddress";

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("");
		System.out.println("UpdateClientAddresses v1.3.9");
		System.out.println("");
		WorkItemImpl customworkItem = new WorkItemImpl();
		
		com.finantix.service.extensions.Util util = new com.finantix.service.extensions.Util();
		try {
			host = util.getPropValue("server") ;
		 User = util.getPropValue("user");
		 Password = util.getPropValue("password");
		}
		catch(Exception ex) {ex.printStackTrace();}

		System.out.println("serverURL:"+serverURL);
		
	//	serverURL = "http://prd-plt-as-04.fx.lan:8080/rest/";
		AddressDTO addreses = (AddressDTO) workItem.getParameter("addressesInfo");
		String clientid = (String) workItem.getParameter("clientid");
		
		String method  = "PUT";
		Map<String, Object> orignalparams = workItem.getParameters();
		System.out.println("executeWorkItem orignalparams addreses: " + addreses);
		// Accept=application/xml;X-Requested-With=XmlHttpRequest
		String hparameters = "ContentType=application/jsonAccept=application/json;Content-Type=application/json;X-Requested-With=XmlHttpRequest";
		
		customworkItem.setParameters(orignalparams);
		customworkItem.setParameter("AuthType", "BASIC");
		customworkItem.setParameter("Username", User);
		customworkItem.setParameter("Password", Password);
		customworkItem.setParameter("ContentType", "application/json");
		
		customworkItem.setParameter("Method", method);

		ObjectMapper mapper = new ObjectMapper();
	
		
		if (addreses != null) {
			
			try {
			customworkItem.setParameter("Url", host + serverURL + clientid +"/addresses"  );
			
			String jsonString = mapper.writeValueAsString(addreses);
			customworkItem.setParameter("ContentData", jsonString);
			
			customworkItem.setParameter("Headers", hparameters);

			System.out.println("executeWorkItem getParameters : " + customworkItem.getParameters());

			super.executeWorkItem(customworkItem, manager);
			System.out.println("6 I executeWorkItem   results: " + results);
			
			 results.put(RESULTS_VALUE,addreses);
			 
			 manager.completeWorkItem(workItem.getId(), results);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("");
			System.out.println("UpdateClientAddresses v1.3.8");
			System.out.println("");
			 
			 
		}
		
		

	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		super.abortWorkItem(workItem, manager);
	}

	private ClassLoader classLoader;
	
	Map<String, Object> results = null;
	

	@Override
	protected void postProcessResult(String result, String resultClass, String contentType,
			Map<String, Object> results) {

		System.out.println("4 postProcessResult  result: " + result);

		if (!StringUtils.isEmpty(resultClass) && !StringUtils.isEmpty(contentType)) {
			try {
				Class<?> clazz = Class.forName(resultClass, true, classLoader);

				System.out.println("5 postProcessResult executeWorkItem  result: " + result +"clazz:"+clazz);

				Object resultObject = transformResult(clazz, contentType, result);

				results.put(PARAM_RESULT, resultObject);
			} catch (Throwable e) {
				throw new RuntimeException("Unable to transform respose to object", e);
			}
		} else {

			results.put(PARAM_RESULT, result);
		}
		System.out.println("5 postProcessResult executeWorkItem  results: " + results);
		this.results = results;

		
	}
	


}
