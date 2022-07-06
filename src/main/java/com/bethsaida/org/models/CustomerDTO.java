package com.bethsaida.org.models;

import java.io.Serializable;

import lombok.Data;


public class CustomerDTO implements Serializable {
	
	private static final long serialVersionUID = 3668933955303726520L;
	
	private Long id;
	private String firstName;
	private String lastName;
	
	public CustomerDTO() {
		super();
	}

	public CustomerDTO(Long id, String firstName, String lastName) {
	  
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	 public Long getId() { 
		 return id; }
	  
    public void setId(Long id) { 
    	this.id = id; }
	 
    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "CustomerDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

	

 }
