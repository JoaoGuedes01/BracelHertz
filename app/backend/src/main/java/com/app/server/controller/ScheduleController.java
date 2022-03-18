package com.app.server.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.server.model.Schedule;
import com.app.server.model.User;
import com.app.server.payload.response.ApiResponse;
import com.app.server.repository.ScheduleRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;

@RestController
@RequestMapping(value = "/api")
public class ScheduleController {

	@Autowired
	ScheduleRepository scheduleRepository;

	@Autowired
	UserRepository userRepository;

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/schedules")
	public List<Schedule> listSchedule(@CurrentUser UserPrincipal currentUser) {
		User userLogged = userRepository.findByUserId(currentUser.getId());
		return scheduleRepository.findByUser(userLogged);
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/schedules")
	public ResponseEntity<ApiResponse> saveSchedule(@Valid @RequestBody Schedule schedule,
			@CurrentUser UserPrincipal currentUser) {
		try {
			User userLogged = userRepository.findByUserId(currentUser.getId());
			schedule.setUser(userLogged);
			scheduleRepository.save(schedule);

			return new ResponseEntity<ApiResponse>(
					new ApiResponse(true, "Schedule created successfully", schedule.getScheduleId()),
					HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@DeleteMapping("/schedules/{scheduleId}")
	public ResponseEntity<ApiResponse> deleteSchedule(@PathVariable(value = "scheduleId") Long scheduleId,
			@CurrentUser UserPrincipal currentUser) {
		try {
			scheduleRepository.deleteSchedule(scheduleId, currentUser.getId());
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Schedule deleted successfully", scheduleId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}
}