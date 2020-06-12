package com.finantix.service.extensions;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finantix.mdl.adbk.core.dto.AddressDTO;
import com.finantix.mdl.adbk.core.dto.ContactDTO;
import com.finantix.mdl.adbk.core.dto.PartyDTO;

public class Test {

	

	public static void main(String[] args) {
		
		
		String firstName = "Joseph";
		String lastName = "George";
		//jsonString = "{   'entityType': 'PER',   'fields': {     'nameFirst': [       '"+firstName+"'     ],     'nameLast': [       '"+lastName+"'     ],     'gender': [       'X'     ],     'vitalStatus': [       'A'     ],     'isAccountHolder': [       'X'     ],     'state': [       'X'     ]   },   'providers': {     'InternalLists': {       'maxResults': 50,       'gap': {         'minScoreToApplyGap': 95,         'absoluteGap': 20,         'relativeGap': 11,         'antiGap': 15       },       'quickSearch': false     },     'InternalLists2': {       'maxResults': 50,       'gap': {         'minScoreToApplyGap': 95,         'absoluteGap': 20,         'relativeGap': 11,         'antiGap': 15       },       'quickSearch': false     },     'InternalLists3': {       'maxResults': 50,       'gap': {         'minScoreToApplyGap': 9,         'absoluteGap': 20,         'relativeGap': 11,         'antiGap': 15       },       'quickSearch': false     },     'InternalLists4': {       'maxResults': 50,       'gap': {         'minScoreToApplyGap': 95,         'absoluteGap': 20,         'relativeGap': 11,         'antiGap': 15       },       'quickSearch': false     },     'bingWeb': {       'maxResults': 100,       'adverseTermsOptionId': 'AdverseTerms',       'quickSearch': false     },     'googleCS': {       'maxResults': 100,       'adverseTermsOptionId': 'AdverseTerms',       'quickSearch': false     }   },   'languages': [     'EN'   ],   'variantLevel': 'Custom',   'variants': [     'Yacoob Anwar',     'Anwar Yacoob'   ],   'caseSensitivity': 'INSENSITIVE' }";
		
		
		String jsonString = " {\"entityType\":\"PER\", \"fields\":{ \"nameFirst\": \""+firstName+"\"     ,    \"nameLast\":        \""+lastName+"\"     ,     \"gender\": [       \"X\"     ],     \"vitalStatus\": [       \"A\"     ],     \"isAccountHolder\": [       \"X\"     ],     \"state\": [       \"X\"     ]   },   \"providers\": {     \"InternalLists\": {       \"maxResults\": 50,       \"gap\": {         \"minScoreToApplyGap\": 95,         \"absoluteGap\": 20,         \"relativeGap\": 11,         \"antiGap\": 15       },       \"quickSearch\": false     },     \"InternalLists2\": {       \"maxResults\": 50,       \"gap\": {         \"minScoreToApplyGap\": 95,         \"absoluteGap\": 20,         \"relativeGap\": 11,         \"antiGap\": 15       },       \"quickSearch\": false     },    \"InternalLists3\": {       \"maxResults\": 50,       \"gap\": {         \"minScoreToApplyGap\": 9,         \"absoluteGap\": 20,         \"relativeGap\": 11,         \"antiGap\": 15       },       \"quickSearch\": false     },     \"InternalLists4\": {       \"maxResults\": 50,       \"gap\": {         \"minScoreToApplyGap\": 95,         \"absoluteGap\": 20,         \"relativeGap\": 11,         \"antiGap\": 15       },       \"quickSearch\": false     },     \"bingWeb\": {       \"maxResults\": 100,       \"adverseTermsOptionId\": \"AdverseTerms\",       \"quickSearch\": false     },     \"googleCS\": {       \"maxResults\": 100,       \"adverseTermsOptionId\": \"AdverseTerms\",       \"quickSearch\": false     }   },   \"languages\": [     \"EN\"   ],   \"variantLevel\": \"Custom\",   \"variants\": [     \""+firstName+" "+ lastName+"\",     \""+lastName+" "+ firstName+"\"   ],   \"caseSensitivity\": \"INSENSITIVE\" }";
		
		
		jsonString = "{\"entityType\":\"PER\",\"fields\":{\"nameFirst\":[\"" + firstName + 
				"\"],\"nameLast\":[\""+ lastName  + 
				"\"],\"gender\":[\"X\"],\"vitalStatus\":[\"A\"],\"isAccountHolder\":[\"X\"],\"state\":[\"X\"]},\"providers\":{\"DowJones\":{\"maxResults\":50,\"gap\":{\"minScoreToApplyGap\":95,\"absoluteGap\":20,\"relativeGap\":10,\"antiGap\":15},\"quickSearch\":false},\"factiva\":{\"maxResults\":100,\"dateRestrictionOption\":\"NoDateRestriction\",\"adverseTermsOptionId\":\"AdverseTerms\",\"quickSearch\":false},\"googleCS\":{\"maxResults\":100,\"quickSearch\":false}},\"languages\":[\"EN\"],\"variantLevel\":\"Custom\","
				+ "\"variants\":[\"" + firstName + " " + lastName + "\",\"" + lastName + " " + firstName +"\"],\"caseSensitivity\":\"INSENSITIVE\"}";
		
		
		
		 System.out.print("jsonString:"+jsonString);
		
		 System.exit(1);
		
			jsonString =" [     {         \"draft\": false,         \"id\": -1,         \"personal\": false,         \"relationshipSubType\": \"other\",         \"toItem\": {             \"type\": \"contactDTO\",             \"addressBookIds\": [                 64,                 70,                 57             ],             \"birthDate\": \"1980-09-22T00:00:00.000+02:00\",             \"createdBy\": \"User31\",             \"createdOn\": \"2019-10-24T06:47:05.391+02:00\",             \"firstName\": \"Steve\",             \"gender\": \"FEMALE\",             \"id\": -1,             \"lastName\": \"Smith\",             \"thumbnail\": \"entity://adbk/thumbnails/-1\",             \"updateVersion\": 1,             \"altitude\": 0,             \"clientStatus\": \"CLIENT\",             \"industryActivities\": [],             \"latitude\": 0,             \"longitude\": 0,             \"partyType\": \"Contact\",             \"clientType\": \"INDIVIDUAL\",             \"status\": \"CLIENT\",             \"usCitizen\": false         }     } ]";
		
		
		PartyDTO[] parties ;
		//PartyDTO party = new PartyDTO();
		 ObjectMapper objectMapper = new ObjectMapper();
		 Map<String, Object> jsonMap = null;
		 String jsonInputData = null;
		 try {
			// jsonMap = objectMapper.readValue(jsonObject,  new TypeReference<Map<String,Object>>(){});
			
			 
			 System.out.println("GetParties executeWorkItem   jsonMap  : " +jsonMap);
			 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 parties =  objectMapper.readValue(jsonString, PartyDTO[].class);

			 System.out.println("GetParties executeWorkItem   parties  : " +parties);
			 System.out.println("GetParties executeWorkItem   parties  : " +parties.length);
			 
			 for(PartyDTO party: parties ) {
				 System.out.println("GetParties executeWorkItem   partyw  :");
				 System.out.print(party.getContent());
				
			
			 }
	      
	       
		} catch (Exception e) {
		}
		
		 System.exit(1);
		
		
		
		
		
		/*String tenantid ="x";
		String user ="y";
		String useremail= "z";
		String	jsonString2 = "{\"tenant\":\""+tenantid+"\",\"username\":\""+user+"\",\"email\":\""+useremail+"\",\"locale\":\"en\"}";
		 System.out.println("executeWorkItem   jsonString2  : " +jsonString2);
		 
		 String OTP = Util.generatorOTP(5);
		 String from ="finantix";
		String message ="Authorization code:"+OTP +" Use it to authorize the change";
		String number ="6591052920";
		jsonString2 = "{\"from\": \""+from+"\",\"text\": \""+message+"\",\"to\": \""+number+"\",\"api_key\": \"e54ec738\",\"api_secret\": \"2c10be8a\"}";
		 System.out.println("executeWorkItem   jsonString2  : " +jsonString2);
			
		 
		 
		// TODO Auto-generated method stub
		String jsonObject = "{\"type\":\"contactDTO\",\"addressBookIds\":[-9],\"birthDate\":\"1958-05-14T00:00:00.000+01:00\",\"firstName\":\"Emma\",\"gender\":\"FEMALE\",\"id\":-1,\"lastName\":\"Schneider\",\"salutation\":\"MRS\",\"thumbnail\":\"entity://adbk/thumbnails/-167\",\"updateVersion\":1,\"altitude\":0.0,\"latitude\":0.0,\"longitude\":0.0,\"clientType\":\"INDIVIDUAL\",\"dateBecomeClient\":\"2010-01-01T00:00:00.000+01:00\",\"nationality\":\"DE\",\"usCitizen\":false}";

		jsonObject = "[{\"addressType\":\"RESIDENCE\",\"city\":\"Stuttgart\",\"country\":\"commonfinance/countrys/DE\",\"id\":-1,\"postalCode\":\"4500\",\"preferred\":true,\"street\":\"Grunwald Strasse\",\"yearsAtAddress\":0}]";
		  objectMapper = new ObjectMapper();
		/// Map<String, Object> jsonMap = null;
		  jsonInputData = null;

			// jsonMap = objectMapper.readValue(jsonObject,  new TypeReference<Map<String,Object>>(){});
			
			 
			 System.out.println("executeWorkItem   jsonMap  : " +jsonMap);
			 ContactDTO contact;
			 AddressDTO[] address;
			try {
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				address = objectMapper.readValue(jsonObject, AddressDTO[].class);
				 System.out.println("executeWorkItem   contact  : " +address);
				// System.out.println("executeWorkItem   contact  : " +contact.getBirthDate());
				 //System.out.println("executeWorkItem   contact  : " +contact.getLastName());
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
			*/
	}

}
