package com.finantix.service.extensions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Properties;
import java.util.Random;

public class Util {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
		new Util().getPropValue("user");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static String result = "";
	static InputStream inputStream;
 
	public  String getPropValues() throws IOException {
 
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			
			String fileName = System.getProperty("jboss.server.config.dir") + "/config.properties";
			
		//	System.out.println("fileName:" +fileName);
					
			inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
 
		//	System.out.println("fileName:" +fileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			Date time = new Date(System.currentTimeMillis());
 
			// get the property value and print it out
			String user = prop.getProperty("user");
			String server = prop.getProperty("server");
			String password = prop.getProperty("password");
		
 
			result = "server List = " + server + ", " + user + ", " + password;
			System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
			
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}
	
	  public static String generatorOTP(int length) 
	  { 
	    System.out.print("Your OTP is : "); 
	              //Creating object of Random class
	    Random obj = new Random(); 
	    char[] otp = new char[length]; 
	    for (int i=0; i<length; i++) 
	    { 
	      otp[i]= (char)(obj.nextInt(10)+48); 
	    } 
	    return String.valueOf(otp);
	  } 
	
	public String getPropValue(String key) throws IOException {

		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			String fileName = System.getProperty("jboss.server.config.dir") + "/config.properties";

			System.out.println("fileName:" + fileName);

			FileInputStream fis = new FileInputStream(fileName);
			prop.load(fis);

			System.out.println("prop:" + prop);

			/*
			 * // inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
			 * 
			 * if (inputStream != null) { prop.load(inputStream); } else { throw new
			 * FileNotFoundException("property file '" + fileName +
			 * "' not found in the classpath"); }
			 */

			Date time = new Date(System.currentTimeMillis());

			// get the property value and print it out
			String keyValue = prop.getProperty(key);

			result = keyValue;
			System.out.println(key + "=" + keyValue + "\nProgram Ran on " + time);

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		return result;
	}

}
