package com.finantix.service.extensions;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class GetClientInfo extends RESTWorkItemHandler {
	// http://prd-cm-dk-03.fx.lan:8080/rest/api/tenant/locales
//	String serverURL = "http://prd-fxc-as-04.fx.lan:8084/rest/adbk/contacts";
	
//	String serverURL ="http://prd-wlt-as-06.fx.lan:86/adbk/contacts";
	String serverURL ="/rest/adbk/contacts";
	String host = "";
	String User = "admin@thedigitalstack.com";
	String Password = "password";
	 private static final String RESULTS_VALUE = "ClientInfo";
	
	

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("GetClientInfo v1.3.8");
		com.finantix.service.extensions.Util util = new com.finantix.service.extensions.Util();
		try {
			host = util.getPropValue("server") ;
		 User = util.getPropValue("user");
		 Password = util.getPropValue("password");
		}
		catch(Exception ex) {ex.printStackTrace();}

		System.out.println("serverURL:"+serverURL);
		WorkItemImpl customworkItem = new WorkItemImpl();

		// serverURL = "http://prd-plt-as-04.fx.lan:8080/rest/";
		String clientid = (String) workItem.getParameter("clientid");
		String method = "GET";

		System.out.println("executeWorkItem orignalparams clientid: " + clientid);

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
		customworkItem.setParameter("Url", host + serverURL + "/" + clientid );
		customworkItem.setParameter("Method", method);

		customworkItem.setParameter("Headers", hparameters);

		System.out.println("3 I executeWorkItem new getParameters: " + customworkItem.getParameters());

		super.executeWorkItem(customworkItem, manager);
		System.out.println("6 I executeWorkItem   results: " + results);

		// {Status=200, StatusMsg=request to endpoint
		// http://prd-fxc-as-04.fx.lan:8084/rest/cms/documents successfully completed ,
		// Result={"id":"1cbba4b9-79dd-40a5-b5be-b02ffdb988b7","updateVersion":13}}

		String jsonObject = (String) results.get("Result");
		 ContactDTO contact = new ContactDTO();
		 ObjectMapper objectMapper = new ObjectMapper();
		 Map<String, Object> jsonMap = null;
		 String jsonInputData = null;
		 try {
			// jsonMap = objectMapper.readValue(jsonObject,  new TypeReference<Map<String,Object>>(){});
			
			 
			 System.out.println("executeWorkItem   jsonMap  : " +jsonMap);
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			  contact =  objectMapper.readValue(jsonObject, ContactDTO.class);
			
			 System.out.println("executeWorkItem   contact  : " +contact.getBirthDate());
			 System.out.println("executeWorkItem   contact  : " +contact.getLastName());
			 

	            if (contact != null) {
	                results.put(RESULTS_VALUE,
	                		contact);

	            }
	                
			 System.out.println("executeWorkItem   results  : " +results);
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

		System.out.println("completeWorkItem  for workItem.getId: " + workItem.getId());
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
	

	/*public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		boolean handleException = false;
// extract required parameters
		String urlStr = (String) workItem.getParameter("Url");
		String method = (String) workItem.getParameter("Method");
		String handleExceptionStr = (String) workItem.getParameter("HandleResponseErrors");
		String resultClass = (String) workItem.getParameter("ResultClass");
		String acceptHeader = (String) workItem.getParameter("AcceptHeader");
		String acceptCharset = (String) workItem.getParameter("AcceptCharset");
		String headers = (String) workItem.getParameter(PARAM_HEADERS);

		if (urlStr == null) {
			throw new IllegalArgumentException("Url is a required parameter");
		}
		if (method == null || method.trim().length() == 0) {
			method = "GET";
		}
		if (handleExceptionStr != null) {
			handleException = Boolean.parseBoolean(handleExceptionStr);
		}
		Map<String, Object> params = workItem.getParameters();

// authentication type from parameters
		AuthenticationType authType = type;
		if (params.get(PARAM_AUTH_TYPE) != null) {
			authType = AuthenticationType.valueOf((String) params.get(PARAM_AUTH_TYPE));
		}
	
// optional timeout config parameters, defaulted to 60 seconds
		Integer connectTimeout = getParamAsInt(params.get(PARAM_CONNECT_TIMEOUT));
		if (connectTimeout == null) {
			connectTimeout = 60000;
		}
		Integer readTimeout = getParamAsInt(params.get(PARAM_READ_TIMEOUT));
		if (readTimeout == null) {
			readTimeout = 60000;
		}
		if (headers == null) {
			headers = "";
		}

		HttpClient httpClient = getHttpClient(readTimeout, connectTimeout);

		Object methodObject = configureRequest(method, urlStr, params, acceptHeader, acceptCharset, headers);
		try {
			HttpResponse response = doRequestWithAuthorization(httpClient, methodObject, params, authType);
			StatusLine statusLine = response.getStatusLine();
			int responseCode = statusLine.getStatusCode();
			Map<String, Object> results = new HashMap<String, Object>();
			HttpEntity respEntity = response.getEntity();
			String responseBody = null;
			String contentType = null;
			if (respEntity != null) {
				responseBody = EntityUtils.toString(respEntity, acceptCharset);

				if (respEntity.getContentType() != null) {
					contentType = respEntity.getContentType().getValue();
				}
			}
			if (responseCode >= 200 && responseCode < 300) {
				postProcessResult(responseBody, resultClass, contentType, results);
				results.put(PARAM_STATUS_MSG,
						"request to endpoint " + urlStr + " successfully completed " + statusLine.getReasonPhrase());
			} else {
				if (handleException) {
					handleException(new RESTServiceException(responseCode, responseBody, urlStr));
				} else {
					this.logger.warn("Unsuccessful response from REST server (status: {}, endpoint: {}, response: {}",
							responseCode, urlStr, responseBody);
					results.put(PARAM_STATUS_MSG, "endpoint " + urlStr + " could not be reached: " + responseBody);
				}
			}
			results.put(PARAM_STATUS, responseCode);

// notify manager that work item has been completed
			manager.completeWorkItem(workItem.getId(), results);
		} catch (Exception e) {
			handleException(e);
		} finally {
			try {
				close(httpClient, methodObject);
			} catch (Exception e) {
				handleException(e);
			}
		}
	}*/

}
