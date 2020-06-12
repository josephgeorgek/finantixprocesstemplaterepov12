package com.finantix.service.extensions;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.finantix.service.extensions.Util;

//com.finantix.service.extensions.FinantixServiceMEDocStatusUpdate
public class GetClientKYC extends RESTWorkItemHandler {

	String serverURL = "state/reviews/";
	String host = "";
	String User = "admin@thedigitalstack.com";
	String Password = "password";
	private static final String RESULTS_VALUE = "riskInfo";

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("***");
		System.out.println("GetClientKYC v1.3.8");
		Util util = new Util();
		try {
			host = util.getPropValue("serverKYC");
			User = util.getPropValue("userKYC");
			Password = util.getPropValue("passwordKYC");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("serverURL:" + serverURL);
		WorkItemImpl customworkItem = new WorkItemImpl();

		// serverURL = "http://prd-plt-as-04.fx.lan:8080/rest/";
		String reviewid = (String) workItem.getParameter("reviewid");
		String method = "GET";

		System.out.println("executeWorkItem orignalparams reviewid: " + reviewid);

		if (reviewid == null) {
			reviewid = "1591189004203";
		} else if (reviewid.isEmpty())
			reviewid = "1591189004203";

		Map<String, Object> orignalparams = workItem.getParameters();

		// Accept=application/xml;X-Requested-With=XmlHttpRequest
		String hparameters = "ContentType=application/jsonAccept=application/json;Content-Type=application/json;X-Requested-With=XmlHttpRequest";

		customworkItem.setParameters(orignalparams);
		customworkItem.setParameter("AuthType", "BASIC");
		customworkItem.setParameter("Username", User);
		customworkItem.setParameter("Password", Password);
		customworkItem.setParameter("ContentType", "application/json");
		customworkItem.setParameter("Url", host + serverURL + reviewid + "/summary");
		customworkItem.setParameter("Method", method);

		customworkItem.setParameter("Headers", hparameters);

		System.out.println("3 I executeWorkItem new getParameters: " + customworkItem.getParameters());

		super.executeWorkItem(customworkItem, manager);
		System.out.println("6 I executeWorkItem   results: " + results);

		String jsonObject = (String) results.get("Result");
		RiskDTO risk = new RiskDTO();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> jsonMap = null;
		String jsonInputData = null;
		try {
			// jsonMap = objectMapper.readValue(jsonObject, new
			// TypeReference<Map<String,Object>>(){});

			System.out.println("executeWorkItem   jsonMap  : " + jsonMap);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			risk = objectMapper.readValue(jsonObject, RiskDTO.class);
			System.out.println("executeWorkItem   getCurrentRisk  : " + risk);
			System.out.println("executeWorkItem   getCurrentRisk  : " + risk.getRisks().getResult().getCurrentRisk());

			if (risk != null) {
				results.put(RESULTS_VALUE, risk.getRisks().getResult().getCurrentRisk());

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

		System.out.println("completeWorkItem  for workItem.getId: " + workItem.getId());
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


}
