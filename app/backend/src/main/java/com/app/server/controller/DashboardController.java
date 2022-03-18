package com.app.server.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.server.model.Prison;
import com.app.server.model.Role;
import com.app.server.model.User;
import com.app.server.payload.request.ListDashboard;
import com.app.server.payload.request.ListDashboardCircle;
import com.app.server.repository.AlertLogRepository;
import com.app.server.repository.PrisonerRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;

@RestController
@RequestMapping(value = "/api")
public class DashboardController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PrisonerRepository prisonerRepository;

	@Autowired
	AlertLogRepository alertLogRepository;

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/dashboard")
	public ListDashboard listDashboard(@CurrentUser UserPrincipal currentUser) {
		User userLogged = userRepository.findByUserId(currentUser.getId());
		Set<Role> roleUserLogged = userLogged.getRoles();
		Prison prison = userLogged.getPrison();

		// Get Permissions
		// !(prison.toString().equals(userLogged.getPrison().toString()))
		if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
			int totalUsers = userRepository.findAllByPrison(prison).size();
			int totalPrisoners = prisonerRepository.findByPrisonOrderByCreatedTimestampAsc(prison).size();
			int totalAlertLogs = alertLogRepository.findAlertLogsByPrison(prison).size();

			ListDashboard dashboard = new ListDashboard(totalUsers, totalPrisoners, totalAlertLogs);

			return dashboard;
		}

		int totalUsers = userRepository.findAll().size();
		int totalPrisoners = prisonerRepository.findAll().size();
		int totalAlertLogs = alertLogRepository.findAll().size();

		ListDashboard dashboard = new ListDashboard(totalUsers, totalPrisoners, totalAlertLogs);

		return dashboard;
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/dashboard-circle")
	public ListDashboardCircle listDashboardCircle(@CurrentUser UserPrincipal currentUser) {
		// Get User Logged
		User userLogged = userRepository.findByUserId(currentUser.getId());
		Set<Role> roleUserLogged = userLogged.getRoles();
		Prison prison = userLogged.getPrison();
		Long prisonId = prison.getPrisonId();
		//
		// Send The Data For Manager
		if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
			int totalPrisonersWithAlerts = alertLogRepository.totalDistinctAlertsInLastMonthByPrisonId(prisonId);
			int totalPrisoners = prisonerRepository.findByPrisonOrderByCreatedTimestampAsc(prison).size();
			int totalPrisonersWithNoAlerts = totalPrisoners - totalPrisonersWithAlerts;

			ListDashboardCircle dashboardCircle = new ListDashboardCircle(totalPrisonersWithAlerts,
					totalPrisonersWithNoAlerts, totalPrisoners);

			return dashboardCircle;
		}
		// Else - Send The Data For Network Manager
		int totalPrisonersWithAlerts = alertLogRepository.totalDistinctAlertsInLastMonth();
		int totalPrisoners = prisonerRepository.findAll().size();
		int totalPrisonersWithNoAlerts = totalPrisoners - totalPrisonersWithAlerts;

		ListDashboardCircle dashboardCircle = new ListDashboardCircle(totalPrisonersWithAlerts,
				totalPrisonersWithNoAlerts, totalPrisoners);

		return dashboardCircle;
	}

}
