package com.finantix.mdl.adbk.core.dto;

import java.util.HashMap;
import java.util.Map;

public class AddressDTO implements java.io.Serializable {

static final long serialVersionUID = 1L;

	private String addressType;
	private String city;
	private String country;
	private Integer id;
	private String postalCode;
	private Boolean preferred;
	private String street;
	private Integer yearsAtAddress;
	
	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Boolean getPreferred() {
		return preferred;
	}

	public void setPreferred(Boolean preferred) {
		this.preferred = preferred;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getYearsAtAddress() {
		return yearsAtAddress;
	}

	public void setYearsAtAddress(Integer yearsAtAddress) {
		this.yearsAtAddress = yearsAtAddress;
	}


public String toString(){//overriding the toString() method  
  return "AddressDTO: "+country+">> "+id;  
 }  

}
