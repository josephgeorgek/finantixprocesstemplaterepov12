package com.finantix.service.extensions;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class Client implements Serializable
{
	private static final long serialVersionUID = 2508430507136805635L;
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private Integer age;
	private Long id;

}
