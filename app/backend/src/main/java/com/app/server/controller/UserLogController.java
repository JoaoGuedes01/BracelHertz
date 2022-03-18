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

import com.app.server.repository.UserLogRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;
import com.app.server.model.Prison;
import com.app.server.model.Role;
import com.app.server.model.User;
import com.app.server.model.UserLog;

@RestController
@RequestMapping(value = "/api")
public class UserLogController {

	@Autowired
	UserLogRepository userLogRepository;

	@Autowired
	UserRepository userRepository;

	@PreAuthorize("hasRole('NETWORKMAN')")
	@GetMapping("/user-logs")
	public List<UserLog> listUserLog() {
		return userLogRepository.findAllByOrderByLogTimestampDesc();
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping("/user-logs/managers")
	public Optional<List<UserLog>> listUserLog(@CurrentUser UserPrincipal currentUser) {
		User userLogged = userRepository.findByUserId(currentUser.getId());
		return userLogRepository.findByUserLogPrisonId(userLogged.getPrison());
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/user-logs/{userId}")
	public Optional<List<UserLog>> listUserLogByUserId(@PathVariable(value = "userId") Long userId,
			@CurrentUser UserPrincipal currentUser) {
		// Get User Logged
		User userLogged = userRepository.findByUserId(currentUser.getId());
		Set<Role> roleUserLogged = userLogged.getRoles();
		Prison userLoggedPrison = userLogged.getPrison();
		//
		// Get User Selected
		User userSelected = userRepository.findByUserId(userId);
		//
		if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
			if (userSelected.getPrison().getPrisonId() == userLoggedPrison.getPrisonId()) {
				return userLogRepository.findByUserLogUserId(userId);
			} else {
				Optional<List<UserLog>> empty = Optional.empty();
				return empty;
			}
		} else {
			return userLogRepository.findByUserLogUserId(userId);
		}
	}

}