package com.app.server.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.server.repository.PrisonRepository;
import com.app.server.model.Prison;
import com.app.server.payload.response.ApiResponse;

@RestController
@RequestMapping(value = "/api")
public class PrisonController {

	@Autowired
	PrisonRepository prisonRepository;

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prisons")
	public List<Prison> listPrison() {
		return prisonRepository.findAllByOrderByPrisonIdAsc();
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prisons/{prisonId}")
	public Prison listPrison(@PathVariable(value = "prisonId") long prisonId) {
		return prisonRepository.findByPrisonId(prisonId);
	}

	@PreAuthorize("hasRole('NETWORKMAN')")
	@PostMapping("/prisons")
	public ResponseEntity<ApiResponse> savePrison(@Valid @RequestBody Prison prison) {
		try {
			if (prisonRepository.existsByPrisonId(prison.getPrisonId())) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Can't update a prison from this route"),
						HttpStatus.BAD_REQUEST);
			}
			prisonRepository.save(prison);
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Prison created", prison.getPrisonId()),
					HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('NETWORKMAN')")
	@PutMapping("/prisons")
	public ResponseEntity<ApiResponse> updatePrison(@Valid @RequestBody Prison prison) {
		try {
			if (!(prisonRepository.existsByPrisonId(prison.getPrisonId()))) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Can't create a prison from this route"),
						HttpStatus.BAD_REQUEST);
			}
			prisonRepository.save(prison);
			return new ResponseEntity<ApiResponse>(
					new ApiResponse(true, "Prison updated successfully", prison.getPrisonId()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

}