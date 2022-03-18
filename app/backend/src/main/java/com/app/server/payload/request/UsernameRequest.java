package com.app.server.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.app.server.util.ConstantUtils;

public class UsernameRequest {
 
	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.USERNAME_PATTERN, message = "Invalid username")
	private String username;
	
	public UsernameRequest() {}

	public UsernameRequest(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
