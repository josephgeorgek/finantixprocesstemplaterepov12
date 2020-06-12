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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.BodyPart;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.drools.core.process.instance.impl.WorkItemImpl;
import org.jbpm.process.workitem.rest.RESTServiceException;
import org.jbpm.process.workitem.rest.RESTWorkItemHandler;
import org.drools.core.util.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

//com.finantix.service.extensions.FinantixServiceMEDocStatusUpdate
public class PostActivity extends RESTWorkItemHandler {

	String serverURL = "/rest/rs/activitys";

	String host = "";
	String user = "admin@thedigitalstack.com";
	String password = "password";
	private static final String RESULTS_VALUE = "reviewid";

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		System.out.println("***");
		System.out.println("PostActivity v1.3.8");
		
	//	http://prd-wlt-as-06.fx.lan:882/
	//	serverURL = "rest/rs/activitys";
		WorkItemImpl customworkItem = new WorkItemImpl();
		Util util = new Util();
		String jsonString = "";
		Map<String, Object> orignalparams = workItem.getParameters();
		System.out.println("executeWorkItem orignalparams : " + orignalparams);

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> jsonMap = null;
		String jsonInputData = null;
		try {
			host = util.getPropValue("server");
			user = util.getPropValue("user");
			password = util.getPropValue("password");
			//serverURL =  host+  serverURL;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String method = "POST";
		// Accept=application/xml;X-Requested-With=XmlHttpRequest
		String hparameters = "ContentTypeX=application/json;Accept=application/json;Content-Type=multipart/mixed; boundary=activity-egdvve9pksq;X-Requested-With=XmlHttpRequest";

		customworkItem.setParameters(orignalparams);
		customworkItem.setParameter("AuthType", "BASIC");
		customworkItem.setParameter("Username", user);
		customworkItem.setParameter("Password", password);
		customworkItem.setParameter("ContentType", "multipart/mixed; boundary=activity-egdvve9pksq");
		customworkItem.setParameter("Url", "http://prd-wlt-as-06.fx.lan:882/rest/rs/activitys");
		customworkItem.setParameter("Method", method);

		System.out.println("executeWorkItem getParameters : " + workItem.getParameters());
		ObjectMapper mapper = new ObjectMapper();
		jsonString = (String) workItem.getParameter("jsonString");

		customworkItem.setParameter("ContentData", jsonString);

		customworkItem.setParameter("Headers", hparameters);

		System.out.println("executeWorkItem getParameters : " + customworkItem.getParameters());

		customworkItem.setParameter("ContentData", jsonString);

		customworkItem.setParameter("Headers", hparameters);

		System.out.println("executeWorkItem host : " + host+ "  serverURL:" +  serverURL);

		//super.executeWorkItem(customworkItem, manager);

		System.out.println("executeWorkItem   jsonString  : " + jsonString);
	
		try {
			
			

			String outputResponse = sendBinary(host, serverURL, user, password, jsonString);

			if (outputResponse != null) {
				results.put(RESULTS_VALUE, outputResponse);

			}

			System.out.println("executeWorkItem   results  : " + results);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		System.out.println("sendBinary completeWorkItem  for workItem.getId: " + workItem.getId());
		System.out.println("***");
		System.out.println("***");
		manager.completeWorkItem(workItem.getId(), results);
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		super.abortWorkItem(workItem, manager);
	}

	private ClassLoader classLoader;

	Map<String, Object> results = new HashMap<String, Object>();

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

	public static void main(String[] arg) {

		try {
			String serverURL = "";
			String binarydata = "{\"actionId\":0,\"t_type\":\"meeting\",\"attachments\":[],\"attachmentsToUpload\":[],\"assignees\":[{\"fullname\":\"John Gladstone\",\"username\":\"User31\"}],\"clients\":[{\"fullname\":\"ABC Ltd\",\"contactId\":44506,\"clientAdvisorName\":\"John Gladstone\"}],\"status\":\"open\",\"startDate\":\"2020-06-16T10:22:02.099Z\",\"category\":\"\",\"subCategory\":\"\",\"creator\":\"User31\",\"creatorName\":\"John Gladstone\",\"clientAdvisorName\":\"John Gladstone\",\"title\":\"Meeting with joseph\",\"description\":\"joseph Meeting Enquiry\",\"creationDate\":\"2020-06-09T10:22:02.210Z\",\"userVisibilityTeam\":\"Zurich_AG__Global_Resources_CIS-Test_SubTeam_CabernetSauvignon\",\"recurrence\":false}";

			//String outputResponse = sendBinary(serverURL,binarydata);

			//System.out.println("4 ----------------------------------------" + outputResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String sendBinary(String host, String serverURL, String user, String password, String binarydata) throws Exception {

		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
		provider.setCredentials(AuthScope.ANY, credentials);

		HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

		HttpPost httppost = new HttpPost(host+  serverURL);

		MultipartEntityBuilder entity2 = MultipartEntityBuilder.create().setMimeSubtype("mixed")
				.addPart(FormBodyPartBuilder.create().setName("activity")
						.setBody(new StringBody(binarydata, ContentType.create("application/json"))).build());

		httppost.setEntity(entity2.build());
		// httppost.setEntity(reqEntity.build());

		System.out.println("executing request " + httppost.getRequestLine());

		HttpResponse response = httpclient.execute(httppost);
		String outputResponse = "";
		System.out.println("1 ----------------------------------------");
		System.out.println(response.getStatusLine());
		System.out.println("2 ----------------------------------------");
		outputResponse = EntityUtils.toString(response.getEntity());
		System.out.println(outputResponse);
		System.out.println("3 ----------------------------------------");

		HttpEntity resEntity = response.getEntity();

		if (resEntity != null) {
			System.out.println("Response content length: " + resEntity.getContentLength());
		}

		return outputResponse;
	}
}
