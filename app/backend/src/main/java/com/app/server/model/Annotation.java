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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.app.server.util.ConstantUtils;

@Entity(name = "annotation")
@Table(name = "annotation")
public class Annotation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long annotationId;

	@ManyToOne
	@JoinColumn(name = "createdBy", referencedColumnName = "userId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "userDestId", referencedColumnName = "userId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User userDest;

	@ManyToOne
	@JoinColumn(name = "prisonDestId", referencedColumnName = "prisonId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Prison prisonDest;

	@ManyToOne
	@JoinColumn(name = "prisonerDestId", referencedColumnName = "prisonerId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Prisoner prisonerDest;

	@ManyToOne
	@JoinColumn(name = "annotationDestId", referencedColumnName = "annotationId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Annotation annotationDest;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.DESCRIPTION_PATTERN, message = "Can only letters, letters with special characters and spaces")
	private String title;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.DESCRIPTION_PATTERN, message = "Can only letters, letters with special characters and spaces")
	private String description;

	@CreationTimestamp
	@Column(columnDefinition = "datetime default CURRENT_TIMESTAMP")
	private LocalDateTime createdTimestamp;

	@UpdateTimestamp
	private LocalDateTime lastUpdatedTimestamp;

	public Annotation() {
	}

	public Annotation(Long annotationId, User createdBy, User userDest, Prison prisonDest, Prisoner prisonerDest,
			Annotation annotationDest, String title, String description, LocalDateTime createdTimestamp,
			LocalDateTime lastUpdatedTimestamp) {
		super();
		this.annotationId = annotationId;
		this.createdBy = createdBy;
		this.userDest = userDest;
		this.prisonDest = prisonDest;
		this.prisonerDest = prisonerDest;
		this.annotationDest = annotationDest;
		this.title = title;
		this.description = description;
		this.createdTimestamp = createdTimestamp;
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

	public Long getAnnotationId() {
		return annotationId;
	}

	public void setAnnotationId(Long annotationId) {
		this.annotationId = annotationId;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getUserDest() {
		return userDest;
	}

	public void setUserDest(User userDest) {
		this.userDest = userDest;
	}

	public Prison getPrisonDest() {
		return prisonDest;
	}

	public void setPrisonDest(Prison prisonDest) {
		this.prisonDest = prisonDest;
	}

	public Prisoner getPrisonerDest() {
		return prisonerDest;
	}

	public void setPrisonerDest(Prisoner prisonerDest) {
		this.prisonerDest = prisonerDest;
	}

	public Annotation getAnnotationDest() {
		return annotationDest;
	}

	public void setAnnotationDest(Annotation annotationDest) {
		this.annotationDest = annotationDest;
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

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public LocalDateTime getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}

	public void setLastUpdatedTimestamp(LocalDateTime lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

}