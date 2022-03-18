package com.app.server.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.server.model.FacialRecognition;
import com.app.server.model.User;
import com.app.server.payload.response.ApiResponse;
import com.app.server.repository.FacialRecognitionRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;

@RestController
@RequestMapping(value = "/api")
public class FacialRecognitionController {

	@Autowired
	FacialRecognitionRepository facialRecognitionRepository;

	@Autowired
	UserRepository userRepository;

	@GetMapping("/facial-recognition-images")
	public List<FacialRecognition> listUsers() {
		List<FacialRecognition> images = facialRecognitionRepository.findAll();
		List<FacialRecognition> facialRecognitionImages = new ArrayList<FacialRecognition>();

		for (FacialRecognition image : images) {
			facialRecognitionImages.add(new FacialRecognition(image.getId(), image.getSecret(), image.getType(), decompressBytes(image.getPicByte()), image.getUser()));
		}

		return facialRecognitionImages;
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PostMapping(value = "/facial-recognition/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> updateUserPhoto(@RequestParam("file") MultipartFile file,
			@RequestParam("passwordInserted") String passwordInserted, @CurrentUser UserPrincipal currentUser) {
		try {
			// Get User Logged
			User userLogged = userRepository.findByUserId(currentUser.getId());
			//

			// Password Validations
			String password = userLogged.getPassword();
			if (!BCrypt.checkpw(passwordInserted, password)) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Passwords don't match"),
						HttpStatus.BAD_REQUEST);
			}
			String encryptedString = encrypt(passwordInserted, secretKey);
			// End of Validations

			// File Validations
			String fileType = file.getContentType();
			Long fileSize = file.getSize();

			if (!((fileType.equals("image/png")) || (fileType.equals("image/jpeg")))) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Image can only be png or jpg"),
						HttpStatus.BAD_REQUEST);
			}

			if (fileSize > 1000000) {
				return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Image can't exceeds 1MB"),
						HttpStatus.BAD_REQUEST);
			}
			// End of Validations

			facialRecognitionRepository.deleteByUser(userLogged.getUserId());
			FacialRecognition img = new FacialRecognition(null, encryptedString, fileType,
					compressBytes(file.getBytes()), userLogged);
			facialRecognitionRepository.save(img);
			Long imgId = img.getId();
			return new ResponseEntity<ApiResponse>(
					new ApiResponse(true, "Image for facial recognition added successfully", imgId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}

		return outputStream.toByteArray();
	}

	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}

	private static String secretKey = "boooooooooom!!!!";
	private static String salt = "ssshhhhhhhhhhh!!!!";

	public static String encrypt(String strToEncrypt, String secret) {
		try {
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

}