package com.app.server.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "facialRecognition")
@Table(name = "facialRecognition")
public class FacialRecognition {

	public FacialRecognition() {
		super();
	}

	public FacialRecognition(Long id, String secret, String type, byte[] picByte, User user) {
		this.id = id;
		this.secret = secret;
		this.type = type;
		this.picByte = picByte;
		this.user = user;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(name = "secret")
	private String secret;

	@Column(name = "type")
	private String type;

	@Column(name = "picByte")
	@Lob
	private byte[] picByte;
	
	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "userId", nullable = true)
	private User user;

	public String getSecret() {
		return secret;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getPicByte() {
		return picByte;
	}

	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}