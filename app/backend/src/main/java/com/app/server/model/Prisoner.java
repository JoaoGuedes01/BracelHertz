package com.app.server.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.app.server.util.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "prisoner")
@Table(name = "prisoner")
public class Prisoner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long prisonerId;

	@Column(unique = true)
	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.USERNAME_PATTERN, message = "Can only contain letters and numbers")
	private String identifierId;

	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Can't be blank")
	private Date birthDate;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.ONLYCHAR_PATTERN, message = "Can only letters or letters with special characters")
	private String nationality;

	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = ConstantUtils.CHAR_PATTERN, message = "Can only letters, letters with special characters and spaces")
	private String name;

	private Long photoId;

	@NotNull
	@Size(max = 9, min = 9, message = "Must contain exacly 9 numbers")
	@Pattern(regexp = ConstantUtils.CODE_PATTERN, message = "Can only contain numbers")
	private String contact;

	@Size(max = 9, min = 9, message = "Must contain exacly 9 numbers")
	@Pattern(regexp = ConstantUtils.CODE_PATTERN, message = "Can only contain numbers")
	private String alternativeContact;

	@NotBlank(message = "Can't be blank")
	private String cell;

	@NotNull(message = "Can´t be blank")
	@Max(3)
	@Min(0)
	private int threatLevel;

	@ManyToOne
	@JoinColumn(name = "prisonId", referencedColumnName = "prisonId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Prison prison;

	@Column(unique = true)
	@NotBlank(message = "Can't be blank")
	private String braceletId;

	@Max(120)
	@Column(columnDefinition = "int default 120")
	@NotNull(message = "Can´t be blank")
	private int maxHB;

	@Min(30)
	@Column(columnDefinition = "int default 30")
	@NotNull(message = "Can´t be blank")
	private int minHB;

	@Column(columnDefinition = "boolean default false")
	private boolean alertOff;

	@ManyToOne
	@JoinColumn(name = "createdBy", referencedColumnName = "userId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User createdBy;

	@CreationTimestamp
	private LocalDateTime createdTimestamp;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "prisoner", orphanRemoval = true)
	private Set<CriminalRecord> criminalRecord = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "prisoner", orphanRemoval = true)
	private Set<MedicalPrescription> medicalPrescription = new HashSet<>();

	public Prisoner() {
	}

	public Prisoner(Long prisonerId, String identifierId, Date birthDate, String nationality, String name, Long photoId,
			String contact, String alternativeContact, String cell, int threatLevel, Prison prison, String braceletId,
			int maxHB, int minHB, boolean alertOff, User createdBy, LocalDateTime createdTimestamp,
			Set<CriminalRecord> criminalRecord, Set<MedicalPrescription> medicalPrescription) {
		super();
		this.prisonerId = prisonerId;
		this.identifierId = identifierId;
		this.birthDate = birthDate;
		this.nationality = nationality;
		this.name = name;
		this.photoId = photoId;
		this.contact = contact;
		this.alternativeContact = alternativeContact;
		this.cell = cell;
		this.threatLevel = threatLevel;
		this.prison = prison;
		this.braceletId = braceletId;
		this.maxHB = maxHB;
		this.minHB = minHB;
		this.alertOff = alertOff;
		this.createdBy = createdBy;
		this.createdTimestamp = createdTimestamp;
		this.criminalRecord = criminalRecord;
		this.medicalPrescription = medicalPrescription;
	}
	
	public Prisoner(Long prisonerId, String identifierId, Date birthDate, String nationality, String name, Long photoId,
			String contact, String alternativeContact, String cell, int threatLevel, Prison prison, String braceletId,
			int maxHB, int minHB, boolean alertOff, User createdBy, LocalDateTime createdTimestamp) {
		super();
		this.prisonerId = prisonerId;
		this.identifierId = identifierId;
		this.birthDate = birthDate;
		this.nationality = nationality;
		this.name = name;
		this.photoId = photoId;
		this.contact = contact;
		this.alternativeContact = alternativeContact;
		this.cell = cell;
		this.threatLevel = threatLevel;
		this.prison = prison;
		this.braceletId = braceletId;
		this.maxHB = maxHB;
		this.minHB = minHB;
		this.alertOff = alertOff;
		this.createdBy = createdBy;
		this.createdTimestamp = createdTimestamp;
	}

	public Long getPrisonerId() {
		return prisonerId;
	}

	public void setPrisonerId(Long prisonerId) {
		this.prisonerId = prisonerId;
	}

	public String getIdentifierId() {
		return identifierId;
	}

	public void setIdentifierId(String identifierId) {
		this.identifierId = identifierId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAlternativeContact() {
		return alternativeContact;
	}

	public void setAlternativeContact(String alternativeContact) {
		this.alternativeContact = alternativeContact;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public int getThreatLevel() {
		return threatLevel;
	}

	public void setThreatLevel(int threatLevel) {
		this.threatLevel = threatLevel;
	}

	public Prison getPrison() {
		return prison;
	}

	public void setPrison(Prison prison) {
		this.prison = prison;
	}

	public String getBraceletId() {
		return braceletId;
	}

	public void setBraceletId(String braceletId) {
		this.braceletId = braceletId;
	}

	public int getMaxHB() {
		return maxHB;
	}

	public void setMaxHB(int maxHB) {
		this.maxHB = maxHB;
	}

	public int getMinHB() {
		return minHB;
	}

	public void setMinHB(int minHB) {
		this.minHB = minHB;
	}

	public boolean isAlertOff() {
		return alertOff;
	}

	public void setAlertOff(boolean alertOff) {
		this.alertOff = alertOff;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Set<CriminalRecord> getCriminalRecord() {
		return criminalRecord;
	}

	public void setCriminalRecord(Set<CriminalRecord> criminalRecord) {
		this.criminalRecord = criminalRecord;
	}

	public Set<MedicalPrescription> getMedicalPrescription() {
		return medicalPrescription;
	}

	public void setMedicalPrescription(Set<MedicalPrescription> medicalPrescription) {
		this.medicalPrescription = medicalPrescription;
	}
}
