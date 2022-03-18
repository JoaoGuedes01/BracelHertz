package com.app.server.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.server.model.AlertLog;
import com.app.server.model.Prison;
import com.app.server.model.Prisoner;
import com.app.server.model.Role;
import com.app.server.model.User;
import com.app.server.payload.response.ApiResponse;
import com.app.server.repository.AlertLogRepository;
import com.app.server.repository.PrisonRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;

@RestController
@RequestMapping(value = "/api")
public class AlertLogController {

	@Autowired
	AlertLogRepository alertLogRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PrisonRepository prisonRepository;

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/alert-logs")
	public List<AlertLog> listAlertLogs(@CurrentUser UserPrincipal currentUser) {
		User userLogged = userRepository.findByUserId(currentUser.getId());
		Set<Role> roleUserLogged = userLogged.getRoles();

		// Get Permissions
		if (String.valueOf(roleUserLogged).equals("[Role [id=0]]")
				|| String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
			return alertLogRepository.findAlertLogsByPrison(userLogged.getPrison());
		}

		return alertLogRepository.findAllByOrderByCreatedTimestampDesc();
	}

	@PreAuthorize("hasRole('NETWORKMAN')")
	@GetMapping("/alert-logs/by-prisons/{prisonId}")
	public List<AlertLog> listAlertLog(@PathVariable(value = "prisonId") Long prisonId) {
		Prison prisonSelected = prisonRepository.findByPrisonId(prisonId);
		return alertLogRepository.findAlertLogsByPrison(prisonSelected);
	}

	@PostMapping("/alert-logs")
	public ResponseEntity<ApiResponse> saveAlertLog(@RequestBody AlertLog alertLog) {
		try {
			// Alert Attributes
			Long alertLogId = alertLog.getAlertLogId();
			Prisoner prisoner = alertLog.getPrisoner();
			LocalDateTime createdTimestamp = LocalDateTime.now();
			Date createdDate = Date.from(createdTimestamp.atZone(ZoneId.systemDefault()).toInstant());
			//
			
			List<AlertLog> l1 = alertLogRepository.findAlertLogsByPrisoner(prisoner);
			if (l1.size() != 0) {
				AlertLog lastAlertLog = l1.get(0);
				LocalDateTime lastCreatedTimestamp = lastAlertLog.getCreatedTimestamp();
				Date lastCreatedDate = Date.from(lastCreatedTimestamp.atZone(ZoneId.systemDefault()).toInstant());
				//
				Calendar c = Calendar.getInstance();
				c.setTime(lastCreatedDate);
				c.add(Calendar.MINUTE, 10);

				Date newLastCreatedDate = c.getTime();
				//

				if (createdDate.compareTo(newLastCreatedDate) > 0) {
					alertLogRepository.save(alertLog);
					return new ResponseEntity<ApiResponse>(new ApiResponse(true, "AlertLog Created", alertLogId),
							HttpStatus.CREATED);
				} else {
					return new ResponseEntity<ApiResponse>(new ApiResponse(true, "AlertLog already created"),
							HttpStatus.OK);
				}
			} else {
				alertLogRepository.save(alertLog);
				return new ResponseEntity<ApiResponse>(new ApiResponse(true, "AlertLog Created", alertLogId),
						HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

}