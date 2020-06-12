package com.finantix.service.extensions;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finantix.mdl.adbk.core.dto.AddressDTO;
import com.finantix.mdl.adbk.core.dto.AddressesDTO;
import com.finantix.mdl.adbk.core.dto.ContactDTO;

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
public class GetClientAddresses extends RESTWorkItemHandler {
	// http://prd-cm-dk-03.fx.lan:8080/rest/api/tenant/locales
	//String serverURL = "http://prd-fxc-as-04.fx.lan:8084/rest/adbk/contacts";
	String serverURL = "/rest/adbk/contacts";
	String host = "";
	String User = "admin@thedigitalstack.com";
	String Password = "password";

	private static final String RESULTS_VALUE = "clientAddress";
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

		System.out.println("");
		System.out.println("GetClientAddresses v1.3.8");
		System.out.println("");
		
		
		
		com.finantix.service.extensions.Util util = new com.finantix.service.extensions.Util();
		try {
			host = util.getPropValue("server");
		 User = util.getPropValue("user");
		 Password = util.getPropValue("password");
		}
		catch(Exception ex) {ex.printStackTrace();}
		System.out.println("serverURL:"+serverURL);
		
		WorkItemImpl customworkItem = new WorkItemImpl();

		// serverURL = "http://prd-plt-as-04.fx.lan:8080/rest/";
		String clientid = (String) workItem.getParameter("clientid");
		String method = "GET";

		System.out.println("1 executeWorkItem orignalparams clientid: " + clientid);

		if (clientid == null) {
			clientid = "-1";
		} else if (clientid.isEmpty())
			clientid = "-1";

		Map<String, Object> orignalparams = workItem.getParameters();

		// Accept=application/xml;X-Requested-With=XmlHttpRequest
		String hparameters = "ContentType=application/jsonAccept=application/json;Content-Type=application/json;X-Requested-With=XmlHttpRequest";

		customworkItem.setParameters(orignalparams);
		customworkItem.setParameter("AuthType", "BASIC");
		customworkItem.setParameter("Username", User);
		customworkItem.setParameter("Password", Password);
		customworkItem.setParameter("ContentType", "application/json");
		customworkItem.setParameter("Url", host + serverURL + "/" + clientid + "/addresses");
		customworkItem.setParameter("Method", method);

		customworkItem.setParameter("Headers", hparameters);

		System.out.println("3 I executeWorkItem new getParameters: " + customworkItem.getParameters());

		super.executeWorkItem(customworkItem, manager);
		System.out.println("6 I executeWorkItem   results: " + results);

		String jsonObject = (String) results.get("Result");
		System.out.println("GetClientAddresses executeWorkItem   jsonObject: " + jsonObject);
		AddressDTO[] address ;
		AddressesDTO addreses = new AddressesDTO();
		 ObjectMapper objectMapper = new ObjectMapper();
		 Map<String, Object> jsonMap = null;
		 String jsonInputData = null;
		 try {
			// jsonMap = objectMapper.readValue(jsonObject,  new TypeReference<Map<String,Object>>(){});
			
			 
			 System.out.println("GetClientAddresses executeWorkItem   jsonMap  : " +jsonMap);
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 address =  objectMapper.readValue(jsonObject, AddressDTO[].class);

			 System.out.println("GetClientAddresses executeWorkItem   contact  : " +addreses);
	            if (address != null) {
	               
			 addreses.setAddresses(address);
			 
			 if (address.length > 0)
			 results.put(RESULTS_VALUE,
					 address[0]);
		
	            }

	           /* if (addreses != null) {
	                results.put(RESULTS_VALUE,
	                		addreses);

	            }*/
	                
			 System.out.println("GetClientAddresses executeWorkItem   results  : " +results);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("GetClientAddresses completeWorkItem  for workItem.getId: " + workItem.getId());
		System.out.println("");
		System.out.println("GetClientAddresses v1.3.8");
		System.out.println("");
		
		manager.completeWorkItem(workItem.getId(), results);
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

		System.out.println("postProcessResult  result: " + result);

		if (!StringUtils.isEmpty(resultClass) && !StringUtils.isEmpty(contentType)) {
			try {
				Class<?> clazz = Class.forName(resultClass, true, classLoader);

				System.out.println("postProcessResult executeWorkItem  result: " + result + "clazz:" + clazz);

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
