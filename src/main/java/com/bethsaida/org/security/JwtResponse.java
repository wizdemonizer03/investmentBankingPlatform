package com.bethsaida.org.security;

import java.io.Serializable;
import java.util.List;

import com.bethsaida.org.models.User;



public class JwtResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3891033329232754371L;
	
	private final String token;
    private String tokenType = "Bearer";
    private String Message = "success";
	//private User user;
	
	
	public JwtResponse(String token) {
		
		super();
		this.token = token;
		
	}

	public String getTokenType()
	{
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getToken() {
		return token;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	
 }
