package com.app.server.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.server.repository.UserLogRepository;
import com.app.server.repository.UserRepository;
import com.app.server.model.Prison;
import com.app.server.model.Role;
import com.app.server.model.User;
import com.app.server.model.UserLog;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;
import com.app.server.payload.request.UpdateUserAsGuard;
import com.app.server.payload.request.UpdateUserAsManager;
import com.app.server.payload.request.UpdateUserPassword;
import com.app.server.payload.request.UpdateUserPasswordAsManager;
import com.app.server.payload.request.UpdateUserPasswordWithToken;
import com.app.server.payload.response.ApiResponse;

@RestController
@RequestMapping(value = "/api")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserLogRepository userLogRepository;

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/users/username-exists/{username}")
	public boolean existsByUsername(@PathVariable(value = "username") String username) {
		return userRepository.existsByUsername(username);
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/users/logged-profiles")
	public User listUserLogged(@CurrentUser UserPrincipal currentUser) {
		Long id = currentUser.getId();
		return userRepository.findByUserId(id);
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/users")
	public List<User> listUsers() {
		return userRepository.findAllByOrderByCreatedTimestampAsc();
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping("/users/{userId}")
	public User listUser(@Valid @PathVariable(value = "userId") Long userId) {
		return userRepository.findByUserId(userId);
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping("/users")
	public ResponseEntity<ApiResponse> saveUser(@Valid @RequestBody User user, @CurrentUser UserPrincipal currentUser) {
		try {
			// User Attributes
			Long userId = user.getUserId();
			String username = user.getUsername().trim();
			String password = user.getPassword();
			Date birthDate = user.getBirthDate();
			String nationality = user.getNationality().trim();
			String address = user.getAddress().trim();
			String location = user.getLocation().trim();
			String name = user.getName().trim();
			String contact = user.getContact();
			String email = user.getEmail().trim();
			Prison prison = user.getPrison();
			LocalDateTime createdTimestamp = user.getCreatedTimestamp();
			Set<Role> roles = user.getRoles();
			Date lastLogin = user.getLastLogin();
			// End of Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			//

			// Validations
			if (String.valueOf(password).length() < 6 || String.valueOf(password).length() > 24) {
				return new ResponseEntity<ApiResponse>(
						new ApiResponse(false, "Password must contain between 6 to 24 characters"),
						HttpStatus.BAD_REQUEST);
			}
			if (userRepository.existsByUsername(username)) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Username already taken"),
						HttpStatus.BAD_REQUEST);
			}

			// Get Permissions
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(String.valueOf(roles).equals("[Role [id=0]]"))) {
					return new ResponseEntity<ApiResponse>(new ApiResponse(false, "A manager can only create guards"),
							HttpStatus.BAD_REQUEST);
				} else {
					prison = userLogged.getPrison();
				}
			} else {
				if (!(String.valueOf(roles).equals("[Role [id=0]]") || String.valueOf(roles).equals("[Role [id=1]]")
						|| String.valueOf(roles).equals("[Role [id=2]]"))) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false,
									"A network manager can only create guards, managers and others network managers"),
							HttpStatus.BAD_REQUEST);
				}
			}
			// End of Validations

			// Encrypt Password
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(password);
			//

			User newUser = new User(userId, username, hashedPassword, birthDate, nationality, address, location, name,
					null, contact, email, prison, createdTimestamp, roles, lastLogin, null);

			// Create User
			userRepository.save(newUser);
			//
			// Log
			String description = "Novo utilizador criado.";
			UserLog userLog = new UserLog(null, userLogged, newUser, description, null);
			userLogRepository.save(userLog);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User created", newUser.getUserId()),
					HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	// This route is not in final version
	// Only for developer purpose
	@PostMapping("/users/devs")
	public ResponseEntity<ApiResponse> saveUserDev(@Valid @RequestBody User user) {
		try {
			// User Attributes
			Long userId = user.getUserId();
			String username = user.getUsername().trim();
			String password = user.getPassword();
			Date birthDate = user.getBirthDate();
			String nationality = user.getNationality().trim();
			String address = user.getAddress().trim();
			String location = user.getLocation().trim();
			String name = user.getName().trim();
			String contact = user.getContact();
			String email = user.getEmail().trim();
			Prison prison = user.getPrison();
			LocalDateTime createdTimestamp = user.getCreatedTimestamp();
			Set<Role> roles = user.getRoles();
			Date lastLogin = user.getLastLogin();
			// End of Attributes

			// Validations
			if (String.valueOf(password).length() < 6 || String.valueOf(password).length() > 24) {
				return new ResponseEntity<ApiResponse>(
						new ApiResponse(false, "Password must contain between 6 to 24 characters"),
						HttpStatus.BAD_REQUEST);
			}
			if (userRepository.existsByUsername(username)) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Username already taken"),
						HttpStatus.BAD_REQUEST);
			}

			if (String.valueOf(roles).equals("[]")) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Needs to insert a role"),
						HttpStatus.BAD_REQUEST);
			}
			// End of Validations

			// Encrypt Password
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(password);
			//

			User newUser = new User(userId, username, hashedPassword, birthDate, nationality, address, location, name,
					null, contact, email, prison, createdTimestamp, roles, lastLogin, null);

			// Create User
			userRepository.save(newUser);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User created", newUser.getUserId()),
					HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('GUARD')")
	@PutMapping("/users/by-guards")
	public ResponseEntity<ApiResponse> updateUserAsGuard(@Valid @RequestBody UpdateUserAsGuard user,
			@CurrentUser UserPrincipal currentUser) {
		try {
			String email = user.getEmail();
			String contact = user.getContact();
			Long userId = currentUser.getId();
			userRepository.updateUserAsGuard(email, contact, userId);
			// Log
			User userLogged = userRepository.findByUserId(userId);
			String description = "O contacto/email do guarda foi alterado.";
			UserLog userLog = new UserLog(null, userLogged, userLogged, description, null);
			userLogRepository.save(userLog);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User updated successfully", userId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PutMapping("/users/managers")
	public ResponseEntity<ApiResponse> updateUserAsManager(@Valid @RequestBody UpdateUserAsManager user,
			@CurrentUser UserPrincipal currentUser) {
		try {
			// User Attributes
			Long userId = user.getUserId();
			Date birthDate = user.getBirthDate();
			String nationality = user.getNationality().trim();
			String address = user.getAddress().trim();
			String location = user.getLocation().trim();
			String name = user.getName().trim();
			String contact = user.getContact();
			String email = user.getEmail().trim();
			Long prisonId = user.getPrisonId();
			// Get User for Update
			User userUpdated = userRepository.findByUserId(userId);
			Set<Role> roles = userUpdated.getRoles();
			//
			// End of Attributes

			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			//

			// Validations
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(String.valueOf(roles).equals("[Role [id=0]]")) && !(userId.equals(currentUser.getId()))) {
					return new ResponseEntity<ApiResponse>(new ApiResponse(false, "A manager can only edit guards"),
							HttpStatus.BAD_REQUEST);
				} else {
					prisonId = userLogged.getPrison().getPrisonId();
				}
			} else {
				if ((!(String.valueOf(roles).equals("[Role [id=0]]") || String.valueOf(roles).equals("[Role [id=1]]")))
						&& !(userId.equals(currentUser.getId()))) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false, "A network manager can only edit guards and managers"),
							HttpStatus.BAD_REQUEST);
				}
			}
			// End of Validations

			// Update User
			userRepository.updateUserAsManager(birthDate, nationality, address, location, name, contact, email,
					prisonId, userId);
			//
			// Log
			String description = "Um ou mais atributos do utilizador foram alterados.";
			UserLog userLog = new UserLog(null, userLogged, userUpdated, description, null);
			userLogRepository.save(userLog);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User updated successfully", userId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PutMapping("/users/passwords")
	public ResponseEntity<ApiResponse> updateUserPasswordAsManager(
			@Valid @RequestBody UpdateUserPasswordAsManager password, @CurrentUser UserPrincipal currentUser) {
		try {
			String newPassword = password.getNewPassword();
			Long userId = password.getUserId();

			// Get User for Update
			User userUpdated = userRepository.findByUserId(userId);
			Set<Role> roles = userUpdated.getRoles();
			//
			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			//

			// Permissions Validations
			if (String.valueOf(roleUserLogged).equals("[Role [id=0]]")) {
				if (userId != currentUser.getId()) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false, "A guard can only edit his own profile"), HttpStatus.BAD_REQUEST);
				}
			} else {
				if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
					if (!(String.valueOf(roles).equals("[Role [id=0]]"))) {
						return new ResponseEntity<ApiResponse>(new ApiResponse(false, "A manager can only edit guards"),
								HttpStatus.BAD_REQUEST);
					}
				} else {
					if (!(String.valueOf(roles).equals("[Role [id=0]]")
							|| String.valueOf(roles).equals("[Role [id=1]]"))) {
						return new ResponseEntity<ApiResponse>(
								new ApiResponse(false, "A network manager can only edit guards and managers"),
								HttpStatus.BAD_REQUEST);
					}
				}
			}
			// End of Validations

			// Encrypt Password
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(newPassword);
			//

			// Update User
			userRepository.updateUserPassword(hashedPassword, userId);
			//
			// Log
			String description = "A password do utilizador foi alterada.";
			UserLog userLog = new UserLog(null, userLogged, userUpdated, description, null);
			userLogRepository.save(userLog);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User password updated successfully", userId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PutMapping("/users/logged-passwords")
	public ResponseEntity<ApiResponse> updateUserLoggedPassword(@Valid @RequestBody UpdateUserPassword password,
			@CurrentUser UserPrincipal currentUser) {
		try {
			String enteredOldPassword = password.getOldPassword();
			String newPassword = password.getNewPassword();
			Long userId = currentUser.getId();

			// Get User Logged
			User userLogged = userRepository.findByUserId(userId);
			String userLoggedPassword = userLogged.getPassword();
			//
			// Password Validations
			String oldPassword = userLoggedPassword;
			if (!BCrypt.checkpw(enteredOldPassword, oldPassword)) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Passwords don't match"),
						HttpStatus.BAD_REQUEST);
			}
			// End of Validations

			// Encrypt Password
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(newPassword);
			//

			// Update User
			userRepository.updateUserPassword(hashedPassword, userId);
			//
			String description = "A password do utilizador foi alterada.";
			UserLog userLog = new UserLog(null, userLogged, userLogged, description, null);
			userLogRepository.save(userLog);
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User password updated successfully", userId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/users/network-managers/forgotten-passwords")
	public ResponseEntity<ApiResponse> updateUserForgottenPassword(
			@Valid @RequestBody UpdateUserPasswordWithToken userPasswordToken) {
		try {
			// Attributes
			String username = userPasswordToken.getUsername().trim();
			String newPassword = userPasswordToken.getNewPassword().trim();
			String token = userPasswordToken.getToken().trim();
			//

			// Validations
			if (!(userRepository.existsByUsername(username))) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Username not found"),
						HttpStatus.BAD_REQUEST);
			}
			// Get User and Roles
			User user = userRepository.getUserByUsername(username);
			Set<Role> roleUser = user.getRoles();
			//
			if (!(String.valueOf(roleUser).equals("[Role [id=2]]"))) {
				return new ResponseEntity<ApiResponse>(
						new ApiResponse(false, "Only network managers have permissions to use this feature"),
						HttpStatus.BAD_REQUEST);
			}

			// Token Validations
			String userToken = user.getPasswordToken();
			if (!(String.valueOf(token).equals(userToken))) {
				if (String.valueOf(userToken).equals("null")) {
					return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Token already used"),
							HttpStatus.BAD_REQUEST);

				} else {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false, "The token doesn't match the current one"), HttpStatus.BAD_REQUEST);
				}

			}
			// End of Validations

			Long userId = user.getUserId();

			// Encrypt Password
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(newPassword);
			//

			token = null;

			userRepository.updateUserPasswordWithToken(hashedPassword, token, userId);

			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User password updated successfully", userId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<ApiResponse> deleteUsers(@PathVariable(value = "userId") Long userId,
			@CurrentUser UserPrincipal currentUser) {
		try {

			// Get User for Delete
			User userUpdated = userRepository.findByUserId(userId);
			String userName = userUpdated.getName();
			String userIdentifier = userUpdated.getUsername();
			Set<Role> roles = userUpdated.getRoles();
			//
			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			Set<Role> roleUserLogged = userLogged.getRoles();
			//
			// Validations
			if (String.valueOf(roleUserLogged).equals("[Role [id=1]]")) {
				if (!(String.valueOf(roles).equals("[Role [id=0]]"))) {
					return new ResponseEntity<ApiResponse>(new ApiResponse(false, "A manager can only delete guards"),
							HttpStatus.BAD_REQUEST);
				}
			} else {
				if (!(String.valueOf(roles).equals("[Role [id=0]]") || String.valueOf(roles).equals("[Role [id=1]]"))) {
					return new ResponseEntity<ApiResponse>(
							new ApiResponse(false, "A network manager can only delete guards and managers"),
							HttpStatus.BAD_REQUEST);
				}
			}
			// End of Validations

			String description = "O utilizador " + userName + " (" + userIdentifier + ") foi eliminado da aplicação.";
			UserLog userLog = new UserLog(null, userLogged, userUpdated, description, null);
			userLogRepository.save(userLog);
			// Delete User
			userRepository.deleteById(userId);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User deleted successfully", userId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

}