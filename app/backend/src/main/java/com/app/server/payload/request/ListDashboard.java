package com.app.server.payload.request;

public class ListDashboard {

	private int totalUsers;

	private int totalPrisoners;

	private int totalAlerts;

	public ListDashboard(int totalUsers, int totalPrisoners, int totalAlerts) {
		super();
		this.totalUsers = totalUsers;
		this.totalPrisoners = totalPrisoners;
		this.totalAlerts = totalAlerts;
	}

	public int getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
	}

	public int getTotalPrisoners() {
		return totalPrisoners;
	}

	public void setTotalPrisoners(int totalPrisoners) {
		this.totalPrisoners = totalPrisoners;
	}

	public int getTotalAlerts() {
		return totalAlerts;
	}

	public void setTotalAlerts(int totalAlerts) {
		this.totalAlerts = totalAlerts;
	}

}
