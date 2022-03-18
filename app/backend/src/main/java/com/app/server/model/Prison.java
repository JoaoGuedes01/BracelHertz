package com.app.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.app.server.util.ConstantUtils;

@Entity(name = "prison")
@Table(name = "prison")
public class Prison {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long prisonId;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.CHAR_PATTERN, message = "Can only letters, letters with special characters and spaces")
	private String name;

	@Pattern(regexp = ConstantUtils.DESCRIPTION_PATTERN, message = "Can only letters, letters with special characters and spaces")
	private String description;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.ADDRESS_PATTERN, message = "Can only letters, letters with special characters, numbers and special characters (\",\", \"º\", \" \")")
	private String address;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.CHAR_PATTERN, message = "Can only letters, letters with special characters and spaces")
	private String location;

	private Long photoId;

	@NotBlank(message = "Can't be blank")
	@Email(message = "Insert a valid email")
	private String email;

	@NotBlank(message = "Can't be blank")
	@Size(max = 9, min = 9, message = "Must contain exacly 9 numbers")
	@Pattern(regexp = ConstantUtils.CODE_PATTERN, message = "Can only contain numbers")
	private String contact;

	public Prison() {
	}

	public Prison(Long prisonId, String name, String description, String address, String location, Long photoId,
			String email, String contact) {
		super();
		this.prisonId = prisonId;
		this.name = name;
		this.description = description;
		this.address = address;
		this.location = location;
		this.photoId = photoId;
		this.email = email;
		this.contact = contact;
	}

	public Long getPrisonId() {
		return prisonId;
	}

	public void setPrisonId(Long prisonId) {
		this.prisonId = prisonId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
}