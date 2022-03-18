package com.app.server.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.server.model.CriminalRecord;
import com.app.server.model.Prisoner;
import com.app.server.model.PrisonerLog;
import com.app.server.model.Role;
import com.app.server.model.RoleName;
import com.app.server.model.User;
import com.app.server.payload.response.ApiResponse;
import com.app.server.repository.CriminalRecordRepository;
import com.app.server.repository.PrisonerLogRepository;
import com.app.server.repository.PrisonerRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;

@RestController
@RequestMapping(value = "/api")
public class CriminalRecordController {

	@Autowired
	CriminalRecordRepository criminalRecordRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PrisonerRepository prisonerRepository;

	@Autowired
	PrisonerLogRepository prisonerLogRepository;

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/criminal-records")
	public ResponseEntity<ApiResponse> saveCriminalRecord(@Valid @RequestBody CriminalRecord criminalRecord,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// Prisoner Attributes
			Long criminalRecordId = criminalRecord.getCriminalRecordId();
			Prisoner prisonerId = criminalRecord.getPrisoner();
			String name = criminalRecord.getName();
			String description = criminalRecord.getDescription();
			Date emissionDate = criminalRecord.getEmissionDate();
			LocalDateTime createdTimestamp = criminalRecord.getCreatedTimestamp();
			LocalDateTime lastUpdatedTimestamp = criminalRecord.getLastUpdatedTimestamp();
			// End of Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			//

			// Get Prisoner Logged
			Prisoner prisonerLog = prisonerRepository.findByPrisonerId(prisonerId.getPrisonerId());
			//
			// Get Permissions
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prisonerLog.getPrison().getPrisonId() == userLogged.getPrison().getPrisonId())) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false, "A manager can only create criminal records inside their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}

			// End of Permissions

			CriminalRecord newCriminalRecord = new CriminalRecord(criminalRecordId, prisonerId, name, description,
					emissionDate, createdTimestamp, lastUpdatedTimestamp);

			// Creating Criminal Record

			criminalRecordRepository.save(newCriminalRecord);

			//

			// Prisoner Log
			String description2 = "Novo Registo Criminal adicionado ao Recluso.";
			PrisonerLog prisonerLogs = new PrisonerLog(null, userLogged, prisonerId, description2, null);
			prisonerLogRepository.save(prisonerLogs);
			//

			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Criminal Record created successfully",
					newCriminalRecord.getCriminalRecordId()), HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PutMapping("/criminal-records")
	public ResponseEntity<ApiResponse> updateCriminalRecord(@RequestBody CriminalRecord criminalRecord,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// Prisoner Attributes
			Long criminalRecordId = criminalRecord.getCriminalRecordId();
			Prisoner prisoner = criminalRecord.getPrisoner();
			Long prisonerId = prisoner.getPrisonerId();
			String name = criminalRecord.getName();
			String description = criminalRecord.getDescription();
			Date emissionDate = criminalRecord.getEmissionDate();
			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			//

			// Get Permissions
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prisoner.getPrison().getPrisonId() == userLogged.getPrison().getPrisonId())) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false, "A manager can only update criminal records inside their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}

			// Update Criminal Record
			criminalRecordRepository.updateCriminalRecordAsManager(name, description, emissionDate, criminalRecordId,
					prisonerId);
			// criminalRecordRepository.save(criminalRecord);
			//
			String description2 = "Um ou mais atributos do criminal record foram atualizados.";
			PrisonerLog prisonerLog = new PrisonerLog(null, userLogged, prisoner, description2, null);
			prisonerLogRepository.save(prisonerLog);

			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Criminal Record updated successfully"),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@DeleteMapping("/criminal-records/{criminalRecordId}")
	public ResponseEntity<ApiResponse> deleteCriminalRecord(
			@PathVariable(value = "criminalRecordId") Long criminalRecordId, @CurrentUser UserPrincipal currentUser) {
		try {
			CriminalRecord criminalRecord = criminalRecordRepository.findByCriminalRecordId(criminalRecordId);
			Prisoner prisonerId = criminalRecord.getPrisoner();

			// Get Prisoner Logged
			Prisoner prisoner = prisonerRepository.findByPrisonerId(prisonerId.getPrison().getPrisonId());
			//
			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			//
			// Get Permissions
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prisoner.getPrison().getPrisonId() == userLogged.getPrison().getPrisonId())) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false, "A manager can only delete criminal records inside their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}

			for (Role userRole : roleUserLogged) {
				if (userRole.getName() == RoleName.ROLE_GUARD) {
					return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Failed to delete Criminal Record"),
							HttpStatus.UNAUTHORIZED);
				}
			}
			
			// Prisoner Log
			String description = "Um registo criminal do recluso foi eliminado.";
			PrisonerLog prisonerLog = new PrisonerLog(null, userLogged, prisonerId, description, null);
			prisonerLogRepository.save(prisonerLog);
			//
			// Deleted Criminal Record
			criminalRecordRepository.deleteById(criminalRecordId);
			//
			return new ResponseEntity<ApiResponse>(
					new ApiResponse(true, "Criminal Record deleted successfully", criminalRecordId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}
}
