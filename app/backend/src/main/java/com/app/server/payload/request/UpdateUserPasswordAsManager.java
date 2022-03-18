package com.app.server.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.app.server.util.ConstantUtils;

public class UpdateUserPasswordAsManager {

	@NotNull(message = "Can't be blank")
	private Long userId;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.PASSWORD_PATTERN, message = "Needs at least 1 UpperCase, 1 LowerCase and 1 Number")
	private String newPassword;

	public UpdateUserPasswordAsManager(Long userId, String newPassword) {
		super();
		this.userId = userId;
		this.newPassword = newPassword;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
