package com.finantix.service.extensions;

import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
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
import java.util.Properties;

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
public class VerifyOTP extends RESTWorkItemHandler {
	// http://prd-cm-dk-03.fx.lan:8080/rest/api/tenant/locales
	
	String serverURL = "/rest/otp/generate/";
	String user = "admin@thedigitalstack.com";
	String password = "password";
	String host = "";
	

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		
		Properties prop = new Properties();
		String propFileName = "config.properties";
		
	
		System.out.println("VerifyOTP v1.3.8");
		WorkItemImpl customworkItem = new WorkItemImpl();
		
	//	serverURL = "http://prd-plt-as-04.fx.lan:8080/rest/";

		
		
		 String  useremail = "";
			String tenantid = "";
		
		String jsonString = "";
		com.finantix.service.extensions.Util util = new com.finantix.service.extensions.Util();
		try {
			host = util.getPropValue("server") ;
		 user = util.getPropValue("user");
		 password = util.getPropValue("password");
		   useremail = util.getPropValue("useremail");
		 tenantid = util.getPropValue("tenantid");
		//jsonString = "{\"tenant\":\""+tenantid+"\",\"username\":\"\""+user+"\",\"email\":\"\""+useremail+"\",\"locale\":\"en\"}";
			jsonString = "{\"tenant\":\""+tenantid+"\",\"username\":\""+user+"\",\"email\":\""+useremail+"\",\"locale\":\"en\"}";
		
		}
		catch(Exception ex) {ex.printStackTrace();}
		String method  = "POST";
		Map<String, Object> orignalparams = workItem.getParameters();
		System.out.println("executeWorkItem orignalparams : " + orignalparams);
		// Accept=application/xml;X-Requested-With=XmlHttpRequest
		String hparameters = "ContentType=application/jsonAccept=application/json;Content-Type=application/json;X-Requested-With=XmlHttpRequest";
		
		customworkItem.setParameters(orignalparams);
		customworkItem.setParameter("AuthType", "BASIC");
		customworkItem.setParameter("Username", user);
		customworkItem.setParameter("Password", password);
		customworkItem.setParameter("ContentType", "application/json");
		customworkItem.setParameter("Url", host + serverURL  );
		customworkItem.setParameter("Method", method);

		ObjectMapper mapper = new ObjectMapper();
		
		
		
	
		
		customworkItem.setParameter("ContentData", jsonString);
		
		customworkItem.setParameter("Headers", hparameters);

		System.out.println("executeWorkItem getParameters : " + customworkItem.getParameters());

		super.executeWorkItem(customworkItem, manager);
		
		customworkItem.setParameter("ContentData", jsonString);
		
		customworkItem.setParameter("Headers", hparameters);

		System.out.println("executeWorkItem getParameters : " + customworkItem.getParameters());

		super.executeWorkItem(customworkItem, manager);
		
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

				System.out.println("postProcessResult executeWorkItem  result: " + result +"clazz:"+clazz);

				Object resultObject = transformResult(clazz, contentType, result);

				results.put(PARAM_RESULT, resultObject);
			} catch (Throwable e) {
				throw new RuntimeException("Unable to transform respose to object", e);
			}
		} else {

			results.put(PARAM_RESULT, result);
		}
		System.out.println("postProcessResult executeWorkItem  results: " + results);
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
