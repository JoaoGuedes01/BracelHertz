package com.app.server.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.app.server.util.ConstantUtils;

public class UpdateUserPasswordWithToken {

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.USERNAME_PATTERN, message = "Invalid username")
	private String username;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.PASSWORD_PATTERN, message = "Needs at least 1 UpperCase, 1 LowerCase and 1 Number")
	private String newPassword;

	@NotBlank(message = "Can't be blank")
	private String token;

	public UpdateUserPasswordWithToken(String username, String newPassword, String token) {
		super();
		this.username = username;
		this.newPassword = newPassword;
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
