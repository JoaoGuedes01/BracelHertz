package com.app.server.controller;

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

import com.app.server.model.MedicalPrescription;
import com.app.server.model.Prisoner;
import com.app.server.model.PrisonerLog;
import com.app.server.model.Role;
import com.app.server.model.User;
import com.app.server.payload.response.ApiResponse;
import com.app.server.repository.MedicalPrescriptionRepository;
import com.app.server.repository.PrisonerLogRepository;
import com.app.server.repository.PrisonerRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;

@RestController
@RequestMapping(value = "/api")
public class MedicalPrescriptionController {

	@Autowired
	MedicalPrescriptionRepository medicalPrescriptionRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PrisonerRepository prisonerRepository;

	@Autowired
	PrisonerLogRepository prisonerLogRepository;

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/medical-prescriptions")
	public ResponseEntity<ApiResponse> savePrescription(@Valid @RequestBody MedicalPrescription prescription,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			//
			// Get Prisoner Selected
			Prisoner prisonerSelected = prisonerRepository.findByPrisonerId(prescription.getPrisoner().getPrisonerId());
			Long prisonerPrisonId = prisonerSelected.getPrison().getPrisonId();
			//
			// Validations
			// Get Permissions
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prisonerPrisonId == userLogged.getPrison().getPrisonId())) {
					return new ResponseEntity<ApiResponse>(new ApiResponse(false,
							"A manager can only create medical prescriptions for prisoners inside their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}
			// End of Validations

			medicalPrescriptionRepository.save(prescription);

			// Prisoner Log
			String description = "Novo cuidado médico a ter adicionado com recluso.";
			PrisonerLog prisonerLog = new PrisonerLog(null, userLogged, prescription.getPrisoner(), description, null);
			prisonerLogRepository.save(prisonerLog);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Medical Prescription created successfully",
					prescription.getPrescriptionId()), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PutMapping("/medical-prescriptions")
	public ResponseEntity<ApiResponse> updatePrescription(@Valid @RequestBody MedicalPrescription prescription,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// Medical Prescription Attributes
			Long prescriptionId = prescription.getPrescriptionId();
			Prisoner prisoner = prescription.getPrisoner();
			Long prisonerId = prisoner.getPrisonerId();
			String name = prescription.getName();
			String description = prescription.getDescription();
			// End of Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			//
			// Get Prisoner Selected
			Prisoner prisonerSelected = prisonerRepository.findByPrisonerId(prisonerId);
			Long prisonerPrisonId = prisonerSelected.getPrison().getPrisonId();
			//

			// Validations
			// Get Permissions
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prisonerPrisonId == userLogged.getPrison().getPrisonId())) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false,
									"A manager can only edit medical prescriptions for prisoners inside their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}
			// End of Validations

			// medicalPrescriptionRepository.save(prescription);
			medicalPrescriptionRepository.updateMedicalPrescriptionAsManager(name, description, prescriptionId,
					prisonerId);

			// Prisoner Log
			String descriptionMedical = "Um cuidado médico a ter do recluso foi editado.";
			PrisonerLog prisonerLog = new PrisonerLog(null, userLogged, prisoner, descriptionMedical, null);
			prisonerLogRepository.save(prisonerLog);
			//
			return new ResponseEntity<ApiResponse>(
					new ApiResponse(true, "Medical Prescription updated successfully", prescriptionId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@DeleteMapping("/medical-prescriptions/{prescriptionId}")
	public ResponseEntity<ApiResponse> deletePrescription(@PathVariable(value = "prescriptionId") Long prescriptionId,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// Get Prescription
			MedicalPrescription prescription = medicalPrescriptionRepository.findByPrescriptionId(prescriptionId);
			//
			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			//

			// Validations
			// Get Permissions
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prescription.getPrisoner().getPrison().getPrisonId() == userLogged.getPrison().getPrisonId())) {
					return new ResponseEntity<ApiResponse>(new ApiResponse(false,
							"A manager can only delete medical prescriptions for prisoners inside their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}
			// End of Validations

			// Prisoner Log
			String description = "Um cuidado médico a ter do recluso foi eliminado.";
			PrisonerLog prisonerLog = new PrisonerLog(null, userLogged, prescription.getPrisoner(), description, null);
			prisonerLogRepository.save(prisonerLog);
			//
			// Deleted Medical Prescription
			medicalPrescriptionRepository.deleteById(prescriptionId);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Medical Prescription deleted successfully",
					prescription.getPrescriptionId()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}
}