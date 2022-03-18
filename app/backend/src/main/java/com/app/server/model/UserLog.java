package com.app.server.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "userLog")
@Table(name = "userLog")
public class UserLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userLogId;

	@ManyToOne
	@JoinColumn(name = "byUser", referencedColumnName = "userId", nullable = true)
	private User byUser;

	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "userId", nullable = true)
	private User user;

	private String description;

	@CreationTimestamp
	private LocalDateTime logTimestamp;

	public UserLog() {
	}

	public UserLog(Long userLogId, User byUser, User user, String description, LocalDateTime logTimestamp) {
		super();
		this.userLogId = userLogId;
		this.byUser = byUser;
		this.user = user;
		this.description = description;
		this.logTimestamp = logTimestamp;
	}

	public Long getUserLogId() {
		return userLogId;
	}

	public void setUserLogId(Long userLogId) {
		this.userLogId = userLogId;
	}

	public User getByUser() {
		return byUser;
	}

	public void setByUser(User byUser) {
		this.byUser = byUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getLogTimestamp() {
		return logTimestamp;
	}

	public void setLogTimestamp(LocalDateTime logTimestamp) {
		this.logTimestamp = logTimestamp;
	}

}