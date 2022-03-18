package com.app.server.payload.request;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.app.server.util.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

public class UpdateUserAsManager {

	@NotNull(message = "Can't be Null")
	private Long userId;

	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Can't be null")
	private Date birthDate;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.ONLYCHAR_PATTERN, message = "Can only letters or letters with special characters")
	private String nationality;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.ADDRESS_PATTERN, message = "Can only letters, letters with special characters, numbers and special characters (\",\", \"º\", \" \")")
	private String address;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.CHAR_PATTERN, message = "Can only letters, letters with special characters and spaces")
	private String location;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.CHAR_PATTERN, message = "Can only letters, letters with special characters and spaces")
	private String name;

	@NotNull
	@Size(max = 9, min = 9, message = "Must contain exacly 9 numbers")
	@Pattern(regexp = ConstantUtils.CODE_PATTERN, message = "Can only contain numbers")
	private String contact;

	@Email(message = "Insert a valid email")
	private String email;

	@NotNull(message = "Can't be null")
	Long prisonId;

	public UpdateUserAsManager(Long userId, Date birthDate, String nationality, String address, String location,
			String name, String contact, String email, Long prisonId) {
		super();
		this.userId = userId;
		this.birthDate = birthDate;
		this.nationality = nationality;
		this.address = address;
		this.location = location;
		this.name = name;
		this.contact = contact;
		this.email = email;
		this.prisonId = prisonId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getPrisonId() {
		return prisonId;
	}

	public void setPrisonId(Long prisonId) {
		this.prisonId = prisonId;
	}

}
