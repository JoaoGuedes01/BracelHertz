package com.app.server.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.app.server.util.ConstantUtils;

public class CreateUserAnnotation {

	@NotNull(message = "Can't be blank")
	private Long userDestId;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.DESCRIPTION_PATTERN, message = "Can only letters, letters with special characters and spaces")
	private String title;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.DESCRIPTION_PATTERN, message = "Can only letters, letters with special characters and spaces")
	private String description;

	public CreateUserAnnotation(Long userDestId, String title, String description) {
		super();
		this.userDestId = userDestId;
		this.title = title;
		this.description = description;
	}

	public Long getUserDestId() {
		return userDestId;
	}

	public void setUserDestId(Long userDestId) {
		this.userDestId = userDestId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
