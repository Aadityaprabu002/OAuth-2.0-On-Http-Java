package com.project.network.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
	@JsonProperty("Token")
	String token;
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
