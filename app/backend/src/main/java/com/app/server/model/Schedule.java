package com.app.server.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "schedule")
@Table(name = "schedule")
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scheduleId;

	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "userId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@NotBlank(message = "Can't be blank")
	private String description;

	@Temporal(TemporalType.DATE)
	@NotNull(message = "Can't be null")
	private Date date;

	public Schedule() {
	}

	public Schedule(Long scheduleId, User user, String description, Date date) {
		super();
		this.scheduleId = scheduleId;
		this.user = user;
		this.description = description;
		this.date = date;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}