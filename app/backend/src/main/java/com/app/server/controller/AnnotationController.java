package com.app.server.controller;

import java.time.LocalDateTime;
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

import com.app.server.model.Annotation;
import com.app.server.model.Prison;
import com.app.server.model.Prisoner;
import com.app.server.model.PrisonerLog;
import com.app.server.model.Role;
import com.app.server.model.User;
import com.app.server.payload.request.CreateCommentAnnotation;
import com.app.server.payload.request.CreatePrisonAnnotation;
import com.app.server.payload.request.CreatePrisonerAnnotation;
import com.app.server.payload.request.CreateUserAnnotation;
import com.app.server.payload.request.UpdateAnnotation;
import com.app.server.payload.response.ApiResponse;
import com.app.server.repository.AnnotationRepository;
import com.app.server.repository.PrisonRepository;
import com.app.server.repository.PrisonerLogRepository;
import com.app.server.repository.PrisonerRepository;
import com.app.server.repository.UserLogRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;

@RestController
@RequestMapping(value = "/api")
public class AnnotationController {

	@Autowired
	AnnotationRepository annotationRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserLogRepository userLogRepository;

	@Autowired
	PrisonerRepository prisonerRepository;

	@Autowired
	PrisonerLogRepository prisonerLogRepository;

	@Autowired
	PrisonRepository prisonRepository;

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prison-annotations")
	public Optional<List<Annotation>> listPrisonsAnnotations(@CurrentUser UserPrincipal currentUser) {
		// Get User Logged
		User userLogged = userRepository.findByUserId(currentUser.getId());
		Set<Role> roleUserLogged = userLogged.getRoles();
		Prison userLoggedPrison = userLogged.getPrison();
		//
		if (String.valueOf(roleUserLogged).equals("[Role [id=0]]")
				|| String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
			return annotationRepository.findByPrisonAnnotations(userLoggedPrison);
		} else {
			return annotationRepository.findByPrisonsAnnotationsNet();
		}
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prison-annotations/{prisonId}")
	public Optional<List<Annotation>> listPrisonAnnotations(@PathVariable(value = "prisonId") Long prisonId,
			@CurrentUser UserPrincipal currentUser) {
		// Get User Logged
		User userLogged = userRepository.findByUserId(currentUser.getId());
		Set<Role> roleUserLogged = userLogged.getRoles();
		Prison userLoggedPrison = userLogged.getPrison();
		//
		// Get Prison Selected
		Prison prisonSelected = prisonRepository.findByPrisonId(prisonId);
		//
		if (String.valueOf(roleUserLogged).equals("[Role [id=0]]")
				|| String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
			if (prisonSelected.getPrisonId() == userLoggedPrison.getPrisonId()) {
				return annotationRepository.findByPrisonAnnotations(prisonSelected);
			} else {
				Optional<List<Annotation>> empty = Optional.empty();
				return empty;
			}
		} else {
			return annotationRepository.findByPrisonAnnotations(prisonSelected);
		}
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prisoner-annotations")
	public Optional<List<Annotation>> listPrisonersAnnotations(@CurrentUser UserPrincipal currentUser) {
		// Get User Logged
		User userLogged = userRepository.findByUserId(currentUser.getId());
		Set<Role> roleUserLogged = userLogged.getRoles();
		Prison userLoggedPrison = userLogged.getPrison();
		//
		if (String.valueOf(roleUserLogged).equals("[Role [id=0]]")
				|| String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
			return annotationRepository.findByPrisonersAnnotations(userLoggedPrison);
		} else {
			return annotationRepository.findByPrisonersAnnotationsNet();
		}
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/prisoner-annotations/{prisonerId}")
	public Optional<List<Annotation>> listPrisonerAnnotations(@PathVariable(value = "prisonerId") Long prisonerId,
			@CurrentUser UserPrincipal currentUser) {
		// Get User Logged
		User userLogged = userRepository.findByUserId(currentUser.getId());
		Set<Role> roleUserLogged = userLogged.getRoles();
		Prison userLoggedPrison = userLogged.getPrison();
		//
		// Get Prison Selected
		Prisoner prisonerSelected = prisonerRepository.findByPrisonerId(prisonerId);
		Prison prisonerSelectedPrison = prisonerSelected.getPrison();
		//
		if (String.valueOf(roleUserLogged).equals("[Role [id=0]]")
				|| String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
			if (prisonerSelectedPrison.getPrisonId() == userLoggedPrison.getPrisonId()) {
				return annotationRepository.findByPrisonerAnnotations(prisonerSelected);
			} else {
				Optional<List<Annotation>> empty = Optional.empty();
				return empty;
			}
		} else {
			return annotationRepository.findByPrisonerAnnotations(prisonerSelected);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/user-annotations/{userId}")
	public Optional<List<Annotation>> listUserAnnotations(@PathVariable(value = "userId") Long userId,
			@CurrentUser UserPrincipal currentUser) {
		// Get User Logged
		User userLogged = userRepository.findByUserId(currentUser.getId());
		Set<Role> roleUserLogged = userLogged.getRoles();
		Prison userLoggedPrison = userLogged.getPrison();
		//
		// Get User Selected
		User userSelected = userRepository.findByUserId(userId);
		Set<Role> roleUserSelected = userSelected.getRoles();
		Prison userSelectedPrison = userSelected.getPrison();
		//
		if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
			if (String.valueOf(roleUserSelected).equals("[Role [id=0]]")
					&& userLoggedPrison.getPrisonId() == userSelectedPrison.getPrisonId()) {
				return annotationRepository.findByUserAnnotations(userSelected);
			} else {
				Optional<List<Annotation>> empty = Optional.empty();
				return empty;
			}
		} else {
			return annotationRepository.findByUserAnnotations(userSelected);
		}
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/comments/annotations/{annotationId}")
	public Optional<List<Annotation>> listCommentsAnnotation(@PathVariable(value = "annotationId") Long annotationId) {
		// Get Annotation Selected
		Annotation annotationSelected = annotationRepository.findByAnnotationId(annotationId);
		//
		return annotationRepository.findByCommentsAnnotation(annotationSelected);
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/prison-annotations")
	public ResponseEntity<ApiResponse> savePrisonAnnotation(
			@Valid @RequestBody CreatePrisonAnnotation createPrisonAnnotation, @CurrentUser UserPrincipal currentUser) {
		try {
			// Prison Annotation Attributes
			Long createdBy = currentUser.getId();
			Long prisonDestId = createPrisonAnnotation.getPrisonDestId();
			String title = createPrisonAnnotation.getTitle();
			String description = createPrisonAnnotation.getDescription();
			// End Of Prison Annotation Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(createdBy);
			Set<Role> roleUserLogged = userLogged.getRoles();
			Long userLoggedPrisonId = userLogged.getPrison().getPrisonId();
			//
			if (String.valueOf(roleUserLogged).equals("[Role [id=0]]")
					|| String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prisonDestId == userLoggedPrisonId)) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false,
									"A guard and a manager can only create annotation about their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}
			//
			// Create Annotation About The Prison
			annotationRepository.createPrisonAnnotation(createdBy, prisonDestId, title, description);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Annotation created"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/prisoner-annotations")
	public ResponseEntity<ApiResponse> savePrisonerAnnotation(
			@Valid @RequestBody CreatePrisonerAnnotation createPrisonerAnnotation,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// Prisoner Annotation Attributes
			Long createdBy = currentUser.getId();
			Long prisonerDestId = createPrisonerAnnotation.getPrisonerDestId();
			String title = createPrisonerAnnotation.getTitle();
			String description = createPrisonerAnnotation.getDescription();
			// End Of Prisoner Annotation Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(createdBy);
			Set<Role> roleUserLogged = userLogged.getRoles();
			Long userLoggedPrisonId = userLogged.getPrison().getPrisonId();
			//
			// Get Prisoner
			Prisoner prisoner = prisonerRepository.findByPrisonerId(prisonerDestId);
			//

			if (String.valueOf(roleUserLogged).equals("[Role [id=0]]")
					|| String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(prisoner.getPrison().getPrisonId() == userLoggedPrisonId)) {
					return new ResponseEntity<ApiResponse>(new ApiResponse(false,
							"A guard and a manager can only create annotation about the prisoners in their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}
			//
			// Create Annotation About The Prisoner
			annotationRepository.createPrisonerAnnotation(createdBy, prisonerDestId, title, description);
			//
			String descriptionLog = "Uma anotação ao recluso foi adicionada.";
			PrisonerLog prisonerLog = new PrisonerLog(null, userLogged, prisoner, descriptionLog, null);
			prisonerLogRepository.save(prisonerLog);

			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Annotation created"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/user-annotations")
	public ResponseEntity<ApiResponse> saveUserAnnotation(@Valid @RequestBody CreateUserAnnotation createUserAnnotation,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// User Annotation Attributes
			Long createdBy = currentUser.getId();
			Long userDestId = createUserAnnotation.getUserDestId();
			String title = createUserAnnotation.getTitle();
			String description = createUserAnnotation.getDescription();
			// End Of User Annotation Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(createdBy);
			Set<Role> roleUserLogged = userLogged.getRoles();
			Long userLoggedPrisonId = userLogged.getPrison().getPrisonId();
			//
			// Get UserDest
			User userDest = userRepository.findByUserId(userDestId);
			Long userDestPrisonId = userDest.getPrison().getPrisonId();
			Set<Role> roleUserDest = userDest.getRoles();
			//
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(userDestPrisonId == userLoggedPrisonId)) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false,
									"A manager can only create annotation about the guards in their prison"),
							HttpStatus.BAD_REQUEST);
				} else {
					if (!String.valueOf(roleUserDest).equals("[Role [id=0]]")) {
						return new ResponseEntity<ApiResponse>(
								new ApiResponse(false,
										"A manager can only create annotation about the guards in their prison"),
								HttpStatus.BAD_REQUEST);
					}
				}
			} else {
				if (String.valueOf(roleUserLogged).equals("[Role [id=2]]")) {
					if (!(String.valueOf(roleUserDest).equals("[Role [id=0]]")
							|| String.valueOf(roleUserDest).equals("[Role [id=1]]"))) {
						return new ResponseEntity<ApiResponse>(
								new ApiResponse(false,
										"A network manager can only create annotation about the guards and managers"),
								HttpStatus.BAD_REQUEST);
					}
				}
			}

			//
			// Create Annotation About The Prison
			annotationRepository.createUserAnnotation(createdBy, userDestId, title, description);
			//

			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Annotation created"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/comments/prison-annotations")
	public ResponseEntity<ApiResponse> saveCommentPrisonAnnotation(
			@Valid @RequestBody CreateCommentAnnotation createCommentAnnotation,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// User Annotation Attributes
			Long createdBy = currentUser.getId();
			Long annotationDestId = createCommentAnnotation.getAnnotationDestId();
			String title = createCommentAnnotation.getTitle();
			String description = createCommentAnnotation.getDescription();
			// End Of User Annotation Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(createdBy);
			Set<Role> roleUserLogged = userLogged.getRoles();
			Long userLoggedPrisonId = userLogged.getPrison().getPrisonId();
			//
			// Get AnnotationDest
			Annotation annotationDest = annotationRepository.findByAnnotationId(annotationDestId);
			Long annotationPrisonId = annotationDest.getPrisonDest().getPrisonId();
			//
			if (String.valueOf(roleUserLogged).equals("[Role [id=0]]")
					|| String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(annotationPrisonId == userLoggedPrisonId)) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false,
									"A guard and a manager can only comment annotations about their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}
			//
			// Create Annotation About The Prison
			annotationRepository.createCommentAnnotation(createdBy, annotationDestId, title, description);
			//

			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Comment created"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/comments/prisoner-annotations")
	public ResponseEntity<ApiResponse> saveCommentPrisonerAnnotation(
			@Valid @RequestBody CreateCommentAnnotation createCommentAnnotation,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// User Annotation Attributes
			Long createdBy = currentUser.getId();
			Long annotationDestId = createCommentAnnotation.getAnnotationDestId();
			String title = createCommentAnnotation.getTitle();
			String description = createCommentAnnotation.getDescription();
			// End Of User Annotation Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(createdBy);
			Set<Role> roleUserLogged = userLogged.getRoles();
			Long userLoggedPrisonId = userLogged.getPrison().getPrisonId();
			//
			// Get AnnotationDest
			Annotation annotationDest = annotationRepository.findByAnnotationId(annotationDestId);
			Long annotationPrisonId = annotationDest.getPrisonerDest().getPrison().getPrisonId();
			//
			if (String.valueOf(roleUserLogged).equals("[Role [id=0]]")
					|| String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(annotationPrisonId == userLoggedPrisonId)) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false,
									"A guard and a manager can only comment annotations about their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}
			//
			// Create Annotation About The Prison
			annotationRepository.createCommentAnnotation(createdBy, annotationDestId, title, description);
			//

			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Comment created"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/comments/user-annotations")
	public ResponseEntity<ApiResponse> saveCommentUserAnnotation(
			@Valid @RequestBody CreateCommentAnnotation createCommentAnnotation,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// User Annotation Attributes
			Long createdBy = currentUser.getId();
			Long annotationDestId = createCommentAnnotation.getAnnotationDestId();
			String title = createCommentAnnotation.getTitle();
			String description = createCommentAnnotation.getDescription();
			// End Of User Annotation Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(createdBy);
			Set<Role> roleUserLogged = userLogged.getRoles();
			Long userLoggedPrisonId = userLogged.getPrison().getPrisonId();
			//
			// Get AnnotationDest
			Annotation annotationDest = annotationRepository.findByAnnotationId(annotationDestId);
			User userDest = annotationDest.getUserDest();
			Set<Role> roleUserDest = userDest.getRoles();
			Long annotationPrisonId = userDest.getPrison().getPrisonId();
			//
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(annotationPrisonId == userLoggedPrisonId)) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false,
									"A manager can only comment annotations about their prison"),
							HttpStatus.BAD_REQUEST);
				}
			} else {
				if (!String.valueOf(roleUserDest).equals("[Role [id=0]]")) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false,
									"A manager can only create annotation about the guards in their prison"),
							HttpStatus.BAD_REQUEST);
				}
			}
			//
			// Create Annotation About The Prison
			annotationRepository.createCommentAnnotation(createdBy, annotationDestId, title, description);
			//

			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Comment created"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PutMapping("/annotations")
	public ResponseEntity<ApiResponse> updateAnnotation(@Valid @RequestBody UpdateAnnotation updateAnnotation,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// Annotation Attributes
			Long userLoggedId = currentUser.getId();
			Long annotationId = updateAnnotation.getAnnotationId();
			String title = updateAnnotation.getTitle();
			String description = updateAnnotation.getDescription();
			LocalDateTime lastUpdatedTimestamp = LocalDateTime.now();
			// End Of Annotation Attributes

			// Get Annotation
			Annotation annotation = annotationRepository.findByAnnotationId(annotationId);
			//
			if (!(userLoggedId == annotation.getCreatedBy().getUserId())) {
				return new ResponseEntity<ApiResponse>(
						new ApiResponse(false, "You are not the owner of this annotation"), HttpStatus.BAD_REQUEST);
			}

			//
			// Create Annotation About The Prison
			annotationRepository.updateAnnotation(title, description, lastUpdatedTimestamp, annotationId);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Annotation updated"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@DeleteMapping("/prisons-annotations/{annotationId}")
	public ResponseEntity<ApiResponse> deleteAnnotation(@PathVariable(value = "annotationId") Long annotationId,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// Annotation Attributes
			Long userLoggedId = currentUser.getId();
			// End Of Annotation Attributes

			// Get Annotation
			Annotation annotation = annotationRepository.findByAnnotationId(annotationId);
			//
			if (!(userLoggedId == annotation.getCreatedBy().getUserId())) {
				return new ResponseEntity<ApiResponse>(
						new ApiResponse(false, "You are not the owner of this annotation"), HttpStatus.BAD_REQUEST);
			}

			//
			// Create Annotation About The Prison
			annotationRepository.deleteById(annotationId);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Anotation deleted"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}
}