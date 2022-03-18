package com.app.server.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.app.server.util.ConstantUtils;

public class UpdateUserPassword {

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.PASSWORD_PATTERN, message = "Needs at least 1 UpperCase, 1 LowerCase and 1 Number")
	private String newPassword;

	@NotBlank(message = "Can't be blank")
	private String oldPassword;

	public UpdateUserPassword(String newPassword, String oldPassword) {
		super();
		this.newPassword = newPassword;
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

}
