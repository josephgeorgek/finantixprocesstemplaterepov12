package com.finantix.mdl.adbk.core.dto;

public class AddressesDTO implements java.io.Serializable {

static final long serialVersionUID = 1L;
	
	AddressDTO[] addresses;

	public AddressDTO[] getAddresses() {
		return addresses;
	}

	public void setAddresses(AddressDTO[] addresses) {
		this.addresses = addresses;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String toString() {// overriding the toString() method
		return "AddressesDTO: " + addresses + ">> ";
	}


}
