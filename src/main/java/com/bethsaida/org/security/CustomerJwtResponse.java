package com.bethsaida.org.security;

import java.io.Serializable;

public class CustomerJwtResponse implements Serializable{

	private static final long serialVersionUID = 4536577721656693132L;
	
	private final String token ;
    private String tokenType = "Bearer";
    
    public CustomerJwtResponse(String token) {
	super();
	this.token = token;
	}
    
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getToken() {
		return token;
	}

}
