package com.finantix.service.extensions;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finantix.mdl.adbk.core.dto.AddressDTO;
import com.finantix.mdl.adbk.core.dto.ContactDTO;
import com.finantix.mdl.adbk.core.dto.ReviewDTO;
import com.finantix.mdl.adbk.core.dto.RiskDTO;

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
public class GetClientKYCSearch extends RESTWorkItemHandler {

	String serverURL = "action/reviews?teamOwner=global";
	
	String host = "";
	String user = "admin@thedigitalstack.com";
	String password = "password";
	private static final String RESULTS_VALUE = "reviewid";

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("***");
		System.out.println("GetClientKYCSearch v1.3.8");
		serverURL ="action/reviews?teamOwner=EY-APAC";
		WorkItemImpl customworkItem = new WorkItemImpl();
		Util util = new Util();
		String jsonString = "";
		Map<String, Object> orignalparams = workItem.getParameters();
		System.out.println("executeWorkItem orignalparams : " + orignalparams);

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> jsonMap = null;
		String jsonInputData = null;
		try {
			host = util.getPropValue("serverKYC");
			user = util.getPropValue("userKYC");
			password = util.getPropValue("passwordKYC");
			String firstName = (String) workItem.getParameter("firstName");
			String lastName = (String) workItem.getParameter("lastName");
					jsonString = " {\"entityType\":\"PER\", \"fields\":{ \"nameFirst\": [ \"" + firstName
					+ "\"  ]   ,    \"nameLast\":      [  \"" + lastName
					+ "\"    ] ,     \"gender\": [       \"X\"     ],     \"vitalStatus\": [       \"A\"     ],     \"isAccountHolder\": [       \"X\"     ],     \"state\": [       \"X\"     ]   },   \"providers\": {     \"InternalLists\": {       \"maxResults\": 50,       \"gap\": {         \"minScoreToApplyGap\": 95,         \"absoluteGap\": 20,         \"relativeGap\": 11,         \"antiGap\": 15       },       \"quickSearch\": false     },     \"InternalLists2\": {       \"maxResults\": 50,       \"gap\": {         \"minScoreToApplyGap\": 95,         \"absoluteGap\": 20,         \"relativeGap\": 11,         \"antiGap\": 15       },       \"quickSearch\": false     },    \"InternalLists3\": {       \"maxResults\": 50,       \"gap\": {         \"minScoreToApplyGap\": 9,         \"absoluteGap\": 20,         \"relativeGap\": 11,         \"antiGap\": 15       },       \"quickSearch\": false     },     \"InternalLists4\": {       \"maxResults\": 50,       \"gap\": {         \"minScoreToApplyGap\": 95,         \"absoluteGap\": 20,         \"relativeGap\": 11,         \"antiGap\": 15       },       \"quickSearch\": false     },     \"bingWeb\": {       \"maxResults\": 100,       \"adverseTermsOptionId\": \"AdverseTerms\",       \"quickSearch\": false     },     \"googleCS\": {       \"maxResults\": 100,       \"adverseTermsOptionId\": \"AdverseTerms\",       \"quickSearch\": false     }   },   \"languages\": [     \"EN\"   ],   \"variantLevel\": \"Custom\",   \"variants\": [     \""
					+ firstName + " " + lastName + "\",     \"" + lastName + " " + firstName
					+ "\"   ],   \"caseSensitivity\": \"INSENSITIVE\" }";

					
					jsonString = "{\"entityType\":\"PER\",\"fields\":{\"nameFirst\":[\"" + firstName + 
							"\"],\"nameLast\":[\""+ lastName  + 
							"\"],\"gender\":[\"X\"],\"vitalStatus\":[\"A\"],\"isAccountHolder\":[\"X\"],\"state\":[\"X\"]},\"providers\":{\"DowJones\":{\"maxResults\":50,\"gap\":{\"minScoreToApplyGap\":95,\"absoluteGap\":20,\"relativeGap\":10,\"antiGap\":15},\"quickSearch\":false},\"factiva\":{\"maxResults\":100,\"dateRestrictionOption\":\"NoDateRestriction\",\"adverseTermsOptionId\":\"AdverseTerms\",\"quickSearch\":false},\"googleCS\":{\"maxResults\":100,\"quickSearch\":false}},\"languages\":[\"EN\"],\"variantLevel\":\"Custom\","
							+ "\"variants\":[\"" + firstName + " " + lastName + "\",\"" + lastName + " " + firstName +"\"],\"caseSensitivity\":\"INSENSITIVE\"}";
				
					
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String method = "POST";
		// Accept=application/xml;X-Requested-With=XmlHttpRequest
		String hparameters = "ContentType=application/jsonAccept=application/json;Content-Type=application/json;X-Requested-With=XmlHttpRequest";

		customworkItem.setParameters(orignalparams);
		customworkItem.setParameter("AuthType", "BASIC");
		customworkItem.setParameter("Username", user);
		customworkItem.setParameter("Password", password);
		customworkItem.setParameter("ContentType", "application/json");
		customworkItem.setParameter("Url", host + serverURL);
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

		System.out.println("executeWorkItem   jsonMap  : " + jsonMap);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String jsonObject = (String) results.get("Result");
		ReviewDTO review = new ReviewDTO();

		try {
			// jsonMap = objectMapper.readValue(jsonObject, new
			// TypeReference<Map<String,Object>>(){});

			System.out.println("executeWorkItem   jsonMap  : " + jsonMap);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			review = objectMapper.readValue(jsonObject, ReviewDTO.class);
			System.out.println("executeWorkItem   review  : " + review);
			System.out.println("executeWorkItem   getReview  : " + review.getReview());

			if (review != null) {
				results.put(RESULTS_VALUE, review.getReview());

			}

			System.out.println("executeWorkItem   results  : " + results);
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
		// manager.completeWorkItem(workItem.getId(), results);

		System.out.println("GetClientKYCSearch completeWorkItem  for workItem.getId: " + workItem.getId());
		System.out.println("***");
		System.out.println("***");
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

				System.out.println("5 postProcessResult executeWorkItem  result: " + result + "clazz:" + clazz);

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

	/*
	 * public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
	 * boolean handleException = false; // extract required parameters String urlStr
	 * = (String) workItem.getParameter("Url"); String method = (String)
	 * workItem.getParameter("Method"); String handleExceptionStr = (String)
	 * workItem.getParameter("HandleResponseErrors"); String resultClass = (String)
	 * workItem.getParameter("ResultClass"); String acceptHeader = (String)
	 * workItem.getParameter("AcceptHeader"); String acceptCharset = (String)
	 * workItem.getParameter("AcceptCharset"); String headers = (String)
	 * workItem.getParameter(PARAM_HEADERS);
	 * 
	 * if (urlStr == null) { throw new
	 * IllegalArgumentException("Url is a required parameter"); } if (method == null
	 * || method.trim().length() == 0) { method = "GET"; } if (handleExceptionStr !=
	 * null) { handleException = Boolean.parseBoolean(handleExceptionStr); }
	 * Map<String, Object> params = workItem.getParameters();
	 * 
	 * // authentication type from parameters AuthenticationType authType = type; if
	 * (params.get(PARAM_AUTH_TYPE) != null) { authType =
	 * AuthenticationType.valueOf((String) params.get(PARAM_AUTH_TYPE)); }
	 * 
	 * // optional timeout config parameters, defaulted to 60 seconds Integer
	 * connectTimeout = getParamAsInt(params.get(PARAM_CONNECT_TIMEOUT)); if
	 * (connectTimeout == null) { connectTimeout = 60000; } Integer readTimeout =
	 * getParamAsInt(params.get(PARAM_READ_TIMEOUT)); if (readTimeout == null) {
	 * readTimeout = 60000; } if (headers == null) { headers = ""; }
	 * 
	 * HttpClient httpClient = getHttpClient(readTimeout, connectTimeout);
	 * 
	 * Object methodObject = configureRequest(method, urlStr, params, acceptHeader,
	 * acceptCharset, headers); try { HttpResponse response =
	 * doRequestWithAuthorization(httpClient, methodObject, params, authType);
	 * StatusLine statusLine = response.getStatusLine(); int responseCode =
	 * statusLine.getStatusCode(); Map<String, Object> results = new HashMap<String,
	 * Object>(); HttpEntity respEntity = response.getEntity(); String responseBody
	 * = null; String contentType = null; if (respEntity != null) { responseBody =
	 * EntityUtils.toString(respEntity, acceptCharset);
	 * 
	 * if (respEntity.getContentType() != null) { contentType =
	 * respEntity.getContentType().getValue(); } } if (responseCode >= 200 &&
	 * responseCode < 300) { postProcessResult(responseBody, resultClass,
	 * contentType, results); results.put(PARAM_STATUS_MSG, "request to endpoint " +
	 * urlStr + " successfully completed " + statusLine.getReasonPhrase()); } else {
	 * if (handleException) { handleException(new RESTServiceException(responseCode,
	 * responseBody, urlStr)); } else { this.logger.
	 * warn("Unsuccessful response from REST server (status: {}, endpoint: {}, response: {}"
	 * , responseCode, urlStr, responseBody); results.put(PARAM_STATUS_MSG,
	 * "endpoint " + urlStr + " could not be reached: " + responseBody); } }
	 * results.put(PARAM_STATUS, responseCode);
	 * 
	 * // notify manager that work item has been completed
	 * manager.completeWorkItem(workItem.getId(), results); } catch (Exception e) {
	 * handleException(e); } finally { try { close(httpClient, methodObject); }
	 * catch (Exception e) { handleException(e); } } }
	 */

}
