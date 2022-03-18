package com.app.server.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "alertLog")
@Table(name = "alertLog")
public class AlertLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long alertLogId;

	@ManyToOne
	@JoinColumn(name = "prisonerId", referencedColumnName = "prisonerId", nullable = true)
	private Prisoner prisoner;

	@CreationTimestamp
	@Column(columnDefinition = "datetime default CURRENT_TIMESTAMP")
	private LocalDateTime createdTimestamp;

	public AlertLog() {
	}

	public AlertLog(Long alertLogId, Prisoner prisoner, LocalDateTime createdTimestamp) {
		super();
		this.alertLogId = alertLogId;
		this.prisoner = prisoner;
		this.createdTimestamp = createdTimestamp;
	}

	public Long getAlertLogId() {
		return alertLogId;
	}

	public void setAlertLogId(Long alertLogId) {
		this.alertLogId = alertLogId;
	}

	public Prisoner getPrisoner() {
		return prisoner;
	}

	public void setPrisoner(Prisoner prisoner) {
		this.prisoner = prisoner;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

}