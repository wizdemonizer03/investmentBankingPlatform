package com.bethsaida.org.models;

import org.springframework.http.HttpStatus;

public class APIResponse {
	
	 private Integer status;
	 private Object message;
	 private String username;

	    public APIResponse() {
	    this.status = HttpStatus.OK.value();
	    this.message = message;
	    this.username = username;
	    }

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public Object getMessage() {
			return message;
		}

		public void setMessage(Object message) {
			this.message = message;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	 
	 
}
