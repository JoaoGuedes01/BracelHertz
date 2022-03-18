package com.app.server.payload.request;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.app.server.util.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

public class UpdatePrisonerAsManager {

	@NotNull(message = "Can't be blank")
	private Long prisonerId;

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

	@NotNull
	@Size(max = 9, min = 9, message = "Must contain exacly 9 numbers")
	@Pattern(regexp = ConstantUtils.CODE_PATTERN, message = "Can only contain numbers")
	private String contact;

	@NotNull
	@Size(max = 9, min = 9, message = "Must contain exacly 9 numbers")
	@Pattern(regexp = ConstantUtils.CODE_PATTERN, message = "Can only contain numbers")
	private String alternativeContact;

	@NotBlank(message = "Can't be blank")
	private String cell;

	@NotNull(message = "Can´t be blank")
	@Max(3)
	@Min(0)
	private int threatLevel;

	@NotNull(message = "Can´t be blank")
	private Long prisonId;

	private String braceletId;

	@Max(120)
	@NotNull(message = "Can´t be blank")
	private int maxHB;

	@Min(30)
	@NotNull(message = "Can´t be blank")
	private int minHB;

	@NotNull(message = "Can´t be blank")
	private boolean alertOff;

	public UpdatePrisonerAsManager() {
	}

	public UpdatePrisonerAsManager(Long prisonerId, String identifierId, Date birthDate, String nationality,
			String name, String contact, String alternativeContact, String cell, int threatLevel, Long prisonId,
			String braceletId, int maxHB, int minHB, boolean alertOff) {
		super();
		this.prisonerId = prisonerId;
		this.identifierId = identifierId;
		this.birthDate = birthDate;
		this.nationality = nationality;
		this.name = name;
		this.contact = contact;
		this.alternativeContact = alternativeContact;
		this.cell = cell;
		this.threatLevel = threatLevel;
		this.prisonId = prisonId;
		this.braceletId = braceletId;
		this.maxHB = maxHB;
		this.minHB = minHB;
		this.alertOff = alertOff;
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

	public Long getPrisonId() {
		return prisonId;
	}

	public void setPrisonId(Long prisonId) {
		this.prisonId = prisonId;
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
}
