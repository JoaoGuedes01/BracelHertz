package com.app.server.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.app.server.model.CriminalRecord;
import com.app.server.model.MedicalPrescription;
import com.app.server.model.Prison;
import com.app.server.model.Prisoner;
import com.app.server.model.PrisonerLog;
import com.app.server.model.Role;
import com.app.server.model.RoleName;
import com.app.server.model.User;
import com.app.server.payload.request.CreatePrisoner;
import com.app.server.payload.request.UpdatePrisonerAsGuard;
import com.app.server.payload.request.UpdatePrisonerAsManager;
import com.app.server.payload.response.ApiResponse;
import com.app.server.repository.CriminalRecordRepository;
import com.app.server.repository.MedicalPrescriptionRepository;
import com.app.server.repository.PrisonerLogRepository;
import com.app.server.repository.PrisonerRepository;
import com.app.server.repository.UserLogRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;

@RestController
@RequestMapping(value = "/api")
public class PrisonerController {

	@Autowired
	PrisonerRepository prisonerRepository;

	@Autowired
	PrisonerLogRepository prisonerLogRepository;
	
	@Autowired
	CriminalRecordRepository criminalRecordRepository;
	
	@Autowired
	MedicalPrescriptionRepository medicalPrescriptionRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserLogRepository userLogRepository;

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prisoners/identifier-exists/{identifierId}")
	public boolean existsByIdentifierId(@PathVariable(value = "identifierId") String identifierId) {
		return prisonerRepository.existsByIdentifierId(identifierId);
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prisoners/bracelet-exists/{braceletId}")
	public boolean existsByBraceletId(@PathVariable(value = "braceletId") String braceletId) {
		return prisonerRepository.existsByBraceletId(braceletId);
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prisoners")
	public List<Prisoner> listPrisoner() {
		return prisonerRepository.findAllByOrderByCreatedTimestampAsc();
	}

	@PreAuthorize("hasRole('GUARD')")
	@GetMapping("/prisoners/by-guards")
	public List<Prisoner> listPrisoner(@CurrentUser UserPrincipal currentUser) {
		User userLogged = userRepository.findByUserId(currentUser.getId());
		return prisonerRepository.findByPrisonOrderByCreatedTimestampAsc(userLogged.getPrison());
	}

	@PreAuthorize("hasRole('GUARD')")
	@GetMapping("/alert-prisoners")
	public Optional<List<Prisoner>> listAlertPrisoners(@CurrentUser UserPrincipal currentUser) {
		User userLogged = userRepository.findByUserId(currentUser.getId());
		return prisonerRepository.findByAlertPrisons(userLogged.getPrison());
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prisoners/{prisonerId}")
	public Prisoner listPrisoner(@PathVariable(value = "prisonerId") Long prisonerId) {
		return prisonerRepository.findByPrisonerId(prisonerId);
	}

	@PreAuthorize("hasRole('GUARD')")
	@GetMapping("/prisoners/by-guards/{prisonerId}")
	public Optional<Prisoner> listPrisoner(@PathVariable(value = "prisonerId") Long prisonerId,
			@CurrentUser UserPrincipal currentUser) {
		User userLogged = userRepository.findByUserId(currentUser.getId());
		return prisonerRepository.findByUserLoggedPrison(prisonerId, userLogged.getPrison());
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/prisoners")
	public ResponseEntity<ApiResponse> savePrisoner(@Valid @RequestBody CreatePrisoner prisoner,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// Prisoner Attributes
			Long prisonerId = prisoner.getPrisoner().getPrisonerId();
			String identifierId = prisoner.getPrisoner().getIdentifierId().trim();
			Date birthDate = prisoner.getPrisoner().getBirthDate();
			String nationality = prisoner.getPrisoner().getNationality().trim();
			String name = prisoner.getPrisoner().getName().trim();
			String contact = prisoner.getPrisoner().getContact().trim();
			String alternativeContact = prisoner.getPrisoner().getAlternativeContact().trim();
			String cell = prisoner.getPrisoner().getCell().trim();
			int threatLevel = prisoner.getPrisoner().getThreatLevel();
			Prison prison = prisoner.getPrisoner().getPrison();
			String bracelhertzId = prisoner.getPrisoner().getBraceletId();
			int maxHB = prisoner.getPrisoner().getMaxHB();
			int minHB = prisoner.getPrisoner().getMinHB();
			boolean alertOff = prisoner.getPrisoner().isAlertOff();
			User createdBy = userRepository.findByUserId(currentUser.getId());
			LocalDateTime createdTimestamp = prisoner.getPrisoner().getCreatedTimestamp();
			List<CriminalRecord> criminalRecordItem = prisoner.getCriminalRecord();
			List<MedicalPrescription> medicalPrescriptionItem = prisoner.getMedicalPrescription();
			// End of Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();

			// Get Permissions
			// !(prison.toString().equals(userLogged.getPrison().toString()))
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prison.getPrisonId() == userLogged.getPrison().getPrisonId())) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false, "A manager can only create prisoners inside their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}
			// End of Permissions

			Prisoner newPrisoner = new Prisoner(prisonerId, identifierId, birthDate, nationality, name, null, contact,
					alternativeContact, cell, threatLevel, prison, bracelhertzId, maxHB, minHB, alertOff, createdBy,
					createdTimestamp);

			// Create User
			prisonerRepository.save(newPrisoner);
			//
			
			// Create Criminal Records for User
			for (CriminalRecord cr : criminalRecordItem) {
				cr.setPrisoner(newPrisoner);
				criminalRecordRepository.save(cr);
			}
			//

			// Create Medical Prescriptions for User
			for (MedicalPrescription mp : medicalPrescriptionItem) {
				mp.setPrisoner(newPrisoner);
				medicalPrescriptionRepository.save(mp);
			}
			//

			// Prisoner Log
			String description = "Novo recluso criado.";
			PrisonerLog prisonerLog = new PrisonerLog(null, userLogged, newPrisoner, description, null);
			prisonerLogRepository.save(prisonerLog);
			//
			return new ResponseEntity<ApiResponse>(
					new ApiResponse(true, "Prisoner created", newPrisoner.getPrisonerId()), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PutMapping("/prisoners")
	public ResponseEntity<ApiResponse> UpdatePrisonerAsManager(@Valid @RequestBody UpdatePrisonerAsManager prisoner,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// Prisoner Attributes
			Long prisonerId = prisoner.getPrisonerId();
			String identifierId = prisoner.getIdentifierId().trim();
			Date birthDate = prisoner.getBirthDate();
			String nationality = prisoner.getNationality().trim();
			String name = prisoner.getName().trim();
			String contact = prisoner.getContact();
			String alternativeContact = prisoner.getAlternativeContact();
			String cell = prisoner.getCell();
			int threatLevel = prisoner.getThreatLevel();
			Long prisonId = prisoner.getPrisonId();
			String bracelhertzId = prisoner.getBraceletId();
			int maxHB = prisoner.getMaxHB();
			int minHB = prisoner.getMinHB();
			boolean alertOff = prisoner.isAlertOff();
			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			Long userLoggedPrisonId = userLogged.getPrison().getPrisonId();
			//
			// Get Prisoner Selected
			Prisoner prisonerSelected = prisonerRepository.findByPrisonerId(prisonerId);
			Long prisonerSelectedPrisonId = prisonerSelected.getPrison().getPrisonId();
			//

			// Validations
			// Get Permissions
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prisonerSelectedPrisonId == userLoggedPrisonId)) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false, "A manager can only update prisoners inside their prison"),
							HttpStatus.BAD_REQUEST);
				} else {
					prisonId = prisonerSelectedPrisonId;
				}
			}
			// End of Validations

			// Update Prisoner
			prisonerRepository.updatePrisonerAsManager(identifierId, birthDate, nationality, name, contact,
					alternativeContact, cell, threatLevel, prisonId, bracelhertzId, maxHB, minHB, alertOff, prisonerId);
			//

			// Prisoner Log
			String description = "Um ou mais atributos do recluso foram atualizados.";
			PrisonerLog prisonerLog = new PrisonerLog(null, userLogged, prisonerRepository.findByPrisonerId(prisonerId),
					description, null);
			prisonerLogRepository.save(prisonerLog);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Prisoner updated successfully", prisonerId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('GUARD')")
	@PutMapping("/prisoners/by-guards")
	public ResponseEntity<ApiResponse> updatePrisonerAsGuard(@Valid @RequestBody UpdatePrisonerAsGuard prisoner,
			@CurrentUser UserPrincipal currentUser) {
		try {
			Long prisonerId = prisoner.getPrisonerId();
			String cell = prisoner.getCell();
			String braceletId = prisoner.getBraceletId();
			int maxHB = prisoner.getMaxHB();
			int minHB = prisoner.getMinHB();
			boolean alertoff = prisoner.isAlertOff();

			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Long userLoggedPrisonId = userLogged.getPrison().getPrisonId();
			//
			// Get Prisoner Selected
			Prisoner prisonerSelected = prisonerRepository.findByPrisonerId(prisonerId);
			Long prisonerSelectedPrisonId = prisonerSelected.getPrison().getPrisonId();
			//
			// Validations
			// Get Permissions
			if (!(userLoggedPrisonId == prisonerSelectedPrisonId)) {
				return new ResponseEntity<ApiResponse>(
						new ApiResponse(false, "A guard can only update prisoners inside their prison"),
						HttpStatus.BAD_REQUEST);
			}
			//

			prisonerRepository.updatePrisonerAsGuard(cell, braceletId, maxHB, minHB, alertoff, prisonerId);

			String description = "Um ou mais atributos do recluso foram atualizados.";
			PrisonerLog prisonerLog = new PrisonerLog(null, userLogged, prisonerSelected, description, null);
			prisonerLogRepository.save(prisonerLog);
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Prisoner updated successfully", prisonerId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@DeleteMapping("/prisoners/{prisonerId}")
	public ResponseEntity<ApiResponse> deletePrisoner(@PathVariable(value = "prisonerId") Long prisonerId,
			@CurrentUser UserPrincipal currentUser) {
		try {

			// Validations
			// Get Permissions
			Prisoner prisoner = prisonerRepository.findByPrisonerId(prisonerId);
			String prisonerIdentifier = prisoner.getIdentifierId();
			String namePrisoner = prisoner.getName();

			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();

			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prisoner.getPrison().getPrisonId() == userLogged.getPrison().getPrisonId())) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false, "A manager can only delete prisoners inside their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}

			for (Role userRole : roleUserLogged) {
				if (userRole.getName() == RoleName.ROLE_GUARD) {
					return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Failed to delete Prisoner"),
							HttpStatus.UNAUTHORIZED);
				}
			}
			// End of Validations

			String description = "O recluso " + namePrisoner + " (" + prisonerIdentifier + ") foi eliminado.";
			PrisonerLog prisonerLog = new PrisonerLog(null, userLogged, prisoner, description, null);
			prisonerLogRepository.save(prisonerLog);

			// Delete Prisoner
			prisonerRepository.deleteById(prisonerId);
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Prisoner deleted successfully", prisonerId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}
}
