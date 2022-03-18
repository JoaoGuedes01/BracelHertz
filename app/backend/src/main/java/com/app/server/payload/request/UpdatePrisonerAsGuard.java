package com.app.server.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdatePrisonerAsGuard {

	@NotNull(message = "Can't be blank")
	private Long prisonerId;

	@NotBlank(message = "Can't be blank")
	private String cell;

	private String braceletId;

	@Max(120)
	@NotNull(message = "Can´t be blank")
	private int maxHB;

	@Min(30)
	@NotNull(message = "Can´t be blank")
	private int minHB;

	@NotNull(message = "Can´t be blank")
	private boolean alertOff;

	public UpdatePrisonerAsGuard() {
	}

	public UpdatePrisonerAsGuard(Long prisonerId, String cell, String braceletId, int maxHB, int minHB,
			boolean alertOff) {
		super();
		this.prisonerId = prisonerId;
		this.cell = cell;
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

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
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
