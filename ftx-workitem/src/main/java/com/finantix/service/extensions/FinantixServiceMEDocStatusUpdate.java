package com.finantix.service.extensions;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public class FinantixServiceMEDocStatusUpdate extends RESTWorkItemHandler {
	// http://prd-cm-dk-03.fx.lan:8080/rest/api/tenant/locales
	String serverURL = "http://prd-fxc-as-04.fx.lan:8084/rest/cms/documents";
	
	String User = "admin@thedigitalstack.com";
	String Password = "password";
	
	

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
	
		System.out.println("FinantixServiceMEDocStatusUpdate v1.3.6");
		WorkItemImpl customworkItem = new WorkItemImpl();
		
	//	serverURL = "http://prd-plt-as-04.fx.lan:8080/rest/";
		String docid = (String) workItem.getParameter("docid");
		String status = (String) workItem.getParameter("status");
		String method  = "GET";

		System.out.println("1 executeWorkItem orignalparams docid: " + docid);
		System.out.println("2 executeWorkItem orignalparams status: " + status);
		
		if (docid == null )
		{
			docid ="5a6fcda2-0f7f-4fbd-8f59-b17f87af75ab";
		}
		else if (docid.isEmpty() )
			docid ="5a6fcda2-0f7f-4fbd-8f59-b17f87af75ab";
	
		Map<String, Object> orignalparams = workItem.getParameters();

		// Accept=application/xml;X-Requested-With=XmlHttpRequest
		String hparameters = "ContentType=application/jsonAccept=application/json;Content-Type=application/json;X-Requested-With=XmlHttpRequest";
		
		customworkItem.setParameters(orignalparams);
		customworkItem.setParameter("AuthType", "BASIC");
		customworkItem.setParameter("Username", User);
		customworkItem.setParameter("Password", Password);
		customworkItem.setParameter("ContentType", "application/json");
		customworkItem.setParameter("Url", serverURL +"/"+docid );
		customworkItem.setParameter("Method", method);
		
	
		customworkItem.setParameter("Headers", hparameters);

		System.out.println("3 I executeWorkItem new getParameters: " + customworkItem.getParameters());

		super.executeWorkItem(customworkItem, manager);
		System.out.println("6 I executeWorkItem   results: " + results);
		
		//{Status=200, StatusMsg=request to endpoint http://prd-fxc-as-04.fx.lan:8084/rest/cms/documents successfully completed , Result={"id":"1cbba4b9-79dd-40a5-b5be-b02ffdb988b7","updateVersion":13}}
		
		
		 String jsonObject = (String) results.get("Result");
		 
			System.out.println(workItem.getId()+ "####### Part 1 DONE ##########");
		 
		 ObjectMapper objectMapper = new ObjectMapper();
		 Map<String, Object> jsonMap = null;
		 String jsonInputData = null;
		 try {
			 jsonMap = objectMapper.readValue(jsonObject,  new TypeReference<Map<String,Object>>(){});
			// System.out.println("Part II  executeWorkItem   jsonMap  : " +jsonMap);
			 jsonMap.put("businessStatus", status);
			 jsonMap.put("status", status);
			 
			 
			  jsonInputData = objectMapper.writeValueAsString(jsonMap);
			 System.out.println("Part II  executeWorkItem   jsonMap  : " +jsonMap);
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
		 customworkItem.setParameter("Url", serverURL);
		 customworkItem.setParameter("Method", "PUT");
		 
		 customworkItem.setParameter("ContentData", jsonInputData);
		 
		 System.out.println("Part II  executeWorkItem   getParameters  : " +customworkItem.getParameters());
		 
		 
		 super.executeWorkItem(customworkItem, manager);
		 
		System.out.println("Part II  executeWorkItem   completeWorkItem  for workItem.getId: " + workItem.getId());
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
