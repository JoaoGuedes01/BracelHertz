package com.app.server.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.app.server.util.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "user")
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(unique = true)
	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.USERNAME_PATTERN, message = "Can only contain letters and numbers")
	private String username;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.PASSWORD_PATTERN, message = "Needs at least 1 UpperCase, 1 LowerCase and 1 Number")
	private String password;

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

	@Column(unique = true)
	private Long photoId;

	@NotBlank(message = "Can't be blank")
	@Size(max = 9, min = 9, message = "Must contain exacly 9 numbers")
	@Pattern(regexp = ConstantUtils.CODE_PATTERN, message = "Can only contain numbers")
	private String contact;

	@Email(message = "Insert a valid email")
	private String email;

	@ManyToOne
	@JoinColumn(name = "prisonId", referencedColumnName = "prisonId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Prison prison;

	@CreationTimestamp
	private LocalDateTime createdTimestamp;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "userRole", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
	private Set<Role> roles = new HashSet<>();

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	private String passwordToken;

	public User() {
	}

	public User(Long userId, String username, String password, Date birthDate, String nationality, String address,
			String location, String name, Long photoId, String contact, String email, Prison prison,
			LocalDateTime createdTimestamp, Set<Role> roles, Date lastLogin, String passwordToken) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.birthDate = birthDate;
		this.nationality = nationality;
		this.address = address;
		this.location = location;
		this.name = name;
		this.photoId = photoId;
		this.contact = contact;
		this.email = email;
		this.prison = prison;
		this.createdTimestamp = createdTimestamp;
		this.roles = roles;
		this.lastLogin = lastLogin;
		this.passwordToken = passwordToken;
	}

	public Long getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public String getNationality() {
		return nationality;
	}

	public String getAddress() {
		return address;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public Long getPhotoId() {
		return photoId;
	}

	public String getContact() {
		return contact;
	}

	public String getEmail() {
		return email;
	}

	public Prison getPrison() {
		return prison;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPrison(Prison prison) {
		this.prison = prison;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getPasswordToken() {
		return passwordToken;
	}

	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}

}