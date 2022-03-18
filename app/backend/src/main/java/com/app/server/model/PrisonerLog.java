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

@Entity(name = "prisonerLog")
@Table(name = "prisonerLog")
public class PrisonerLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long prisonerLogId;

	@ManyToOne
	@JoinColumn(name = "byUser", referencedColumnName = "userId", nullable = true)
	private User byUser;

	@ManyToOne
	@JoinColumn(name = "prisonerId", referencedColumnName = "prisonerId", nullable = true)
	private Prisoner prisoner;

	private String description;

	@CreationTimestamp
	private LocalDateTime logTimestamp;

	public PrisonerLog() {
	}

	public PrisonerLog(Long prisonerLogId, User byUser, Prisoner prisoner, String description,
			LocalDateTime logTimestamp) {
		super();
		this.prisonerLogId = prisonerLogId;
		this.byUser = byUser;
		this.prisoner = prisoner;
		this.description = description;
		this.logTimestamp = logTimestamp;
	}

	public Long getPrisonerLogId() {
		return prisonerLogId;
	}

	public void setPrisonerLogId(Long prisonerLogId) {
		this.prisonerLogId = prisonerLogId;
	}

	public User getByUser() {
		return byUser;
	}

	public void setByUser(User byUser) {
		this.byUser = byUser;
	}

	public Prisoner getPrisoner() {
		return prisoner;
	}

	public void setPrisoner(Prisoner prisoner) {
		this.prisoner = prisoner;
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
