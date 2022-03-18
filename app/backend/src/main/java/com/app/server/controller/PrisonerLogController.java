package com.app.server.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.server.repository.PrisonerLogRepository;
import com.app.server.repository.PrisonerRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;
import com.app.server.model.Prison;
import com.app.server.model.Prisoner;
import com.app.server.model.PrisonerLog;
import com.app.server.model.Role;
import com.app.server.model.User;

@RestController
@RequestMapping(value = "/api")
public class PrisonerLogController {

	@Autowired
	PrisonerRepository prisonerRepository;

	@Autowired
	PrisonerLogRepository prisonerLogRepository;

	@Autowired
	UserRepository userRepository;

	@PreAuthorize("hasRole('NETWORKMAN')")
	@GetMapping("/prisoner-logs")
	public List<PrisonerLog> listPrisonerLog() {
		return prisonerLogRepository.findAllByOrderByLogTimestampDesc();
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping("/prisoner-logs/managers")
	public Optional<List<PrisonerLog>> listPrisonerLog(@CurrentUser UserPrincipal currentUser) {
		User userLogged = userRepository.findByUserId(currentUser.getId());
		return prisonerLogRepository.findByPrisonerLogPrisonId(userLogged.getPrison());
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prisoner-logs/{prisonerId}")
	public Optional<List<PrisonerLog>> listPrisonerLogByPrisonerId(@PathVariable(value = "prisonerId") Long prisonerId,
			@CurrentUser UserPrincipal currentUser) {
		// Get User Logged
		User userLogged = userRepository.findByUserId(currentUser.getId());
		Set<Role> roleUserLogged = userLogged.getRoles();
		Prison userLoggedPrison = userLogged.getPrison();
		//
		// Get User Selected
		Prisoner prisonerSelected = prisonerRepository.findByPrisonerId(prisonerId);
		//
		if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
			if (prisonerSelected.getPrison().getPrisonId() == userLoggedPrison.getPrisonId()) {
				return prisonerLogRepository.findByPrisonerLogPrisonerId(prisonerId);
			} else {
				Optional<List<PrisonerLog>> empty = Optional.empty();
				return empty;
			}
		} else {
			return prisonerLogRepository.findByPrisonerLogPrisonerId(prisonerId);
		}
	}

}