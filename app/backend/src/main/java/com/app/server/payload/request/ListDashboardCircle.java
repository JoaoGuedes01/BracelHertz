package com.app.server.payload.request;

public class ListDashboardCircle {

	private int totalPrisonersWithAlerts;
	
	private int totalPrisonersWithNoAlerts;
	
	private int totalPrisoners;

	public ListDashboardCircle(int totalPrisonersWithAlerts, int totalPrisonersWithNoAlerts, int totalPrisoners) {
		super();
		this.totalPrisonersWithAlerts = totalPrisonersWithAlerts;
		this.totalPrisonersWithNoAlerts = totalPrisonersWithNoAlerts;
		this.totalPrisoners = totalPrisoners;
	}

	public int getTotalPrisonersWithAlerts() {
		return totalPrisonersWithAlerts;
	}

	public void setTotalPrisonersWithAlerts(int totalPrisonersWithAlerts) {
		this.totalPrisonersWithAlerts = totalPrisonersWithAlerts;
	}

	public int getTotalPrisonersWithNoAlerts() {
		return totalPrisonersWithNoAlerts;
	}

	public void setTotalPrisonersWithNoAlerts(int totalPrisonersWithNoAlerts) {
		this.totalPrisonersWithNoAlerts = totalPrisonersWithNoAlerts;
	}

	public int getTotalPrisoners() {
		return totalPrisoners;
	}

	public void setTotalPrisoners(int totalPrisoners) {
		this.totalPrisoners = totalPrisoners;
	}

	
	
	
}
