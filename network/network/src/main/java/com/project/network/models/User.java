package com.project.network.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	@JsonProperty("Email")
	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
