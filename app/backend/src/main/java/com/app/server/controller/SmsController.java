package com.app.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.app.server.service.SmsService;
import com.app.server.model.Role;
import com.app.server.model.User;
import com.app.server.payload.request.SmsRequest;
import com.app.server.payload.request.UsernameRequest;
import com.app.server.payload.response.ApiResponse;
import com.app.server.repository.UserRepository;

import java.util.Random;
import java.util.Set;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class SmsController {

	private final SmsService service;

	@Autowired
	UserRepository userRepository;

	@Autowired
	public SmsController(SmsService service) {
		this.service = service;
	}

	@PostMapping("/sms")
	public ResponseEntity<ApiResponse> sendSms(@Valid @RequestBody UsernameRequest usernameRequest) {
		try {
			// Attributes
			String username = usernameRequest.getUsername().trim();
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
			// End of Validations

			Long userId = user.getUserId();

			// Generate Random String
			String abc = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			Random r = new Random();
			int size = abc.length();
			String randomString = "";
			for (int i = 0; i < 8; i++) {
				randomString += abc.charAt(r.nextInt(size));
			}
			//

			String newPasswordToken = randomString;

			// Updated Password Token
			userRepository.updateUserPasswordToken(newPasswordToken, userId);
			//

			// Send SMS
			String message = "Olá, eu sou o Bot de SMSs da BracelHertZ. Este é o teu código: " + newPasswordToken;
			String phoneNumber = "+351" + user.getContact();
			SmsRequest smsRequest = new SmsRequest(phoneNumber, message);
			service.sendSms(smsRequest);
			//
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Sms sent"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}
}
