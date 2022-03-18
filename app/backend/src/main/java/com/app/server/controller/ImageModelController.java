package com.app.server.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.server.repository.ImageModelRepository;
import com.app.server.repository.PrisonRepository;
import com.app.server.repository.PrisonerRepository;
import com.app.server.repository.UserRepository;
import com.app.server.security.CurrentUser;
import com.app.server.security.UserPrincipal;
import com.app.server.model.ImageModel;
import com.app.server.model.Prisoner;
import com.app.server.model.Role;
import com.app.server.model.User;
import com.app.server.payload.response.ApiResponse;

@RestController
@RequestMapping(value = "/api")
public class ImageModelController {

	@Autowired
	ImageModelRepository imageRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PrisonerRepository prisonerRepository;

	@Autowired
	PrisonRepository prisonRepository;

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@GetMapping(path = { "/photos/{imageId}" })
	public ImageModel getImage(@PathVariable("imageId") Long imageName) throws IOException {

		final Optional<ImageModel> retrievedImage = imageRepository.findById(imageName);
		ImageModel img = new ImageModel(null, retrievedImage.get().getName(), retrievedImage.get().getType(),
				decompressBytes(retrievedImage.get().getPicByte()));
		return img;
	}

	@PreAuthorize("hasRole('GUARD') or hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PutMapping(value = "/users/upload-photos/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> updateUserPhoto(@PathVariable(value = "userId") Long userId,
			@RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
		try {
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
					if (!(String.valueOf(roles).equals("[Role [id=0]]")) && !(userId.equals(currentUser.getId()))) {
						return new ResponseEntity<ApiResponse>(new ApiResponse(false, "A manager can only edit guards"),
								HttpStatus.BAD_REQUEST);
					}
				}
			}
			//
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

			// File Name
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-z");
			String strDate = formatter.format(date);
			String id = Long.toString(userId);
			String fileName = "profilePhoto-" + id + "-" + strDate + ".png";
			//
			imageRepository.deleteUserPhoto(userId);
			ImageModel img = new ImageModel(null, fileName, fileType, compressBytes(file.getBytes()));
			imageRepository.save(img);
			Long imgId = img.getId();
			userRepository.updateUserPhotoId(imgId, userId);
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Image updated successfully", imgId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('MANAGER') or hasRole('NETWORKMAN')")
	@PutMapping(value = "/prisoners/upload-photos/{prisonerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> updatePrisonerPhoto(@PathVariable(value = "prisonerId") Long prisonerId,
			@RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
		try {
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
				}
			}
			//
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

			// File Name
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-z");
			String strDate = formatter.format(date);
			String id = Long.toString(prisonerId);
			String fileName = "prisonerPhoto-" + id + "-" + strDate + ".png";
			//
			imageRepository.deletePrisonerPhoto(prisonerId);
			ImageModel img = new ImageModel(null, fileName, fileType, compressBytes(file.getBytes()));
			imageRepository.save(img);
			Long imgId = img.getId();
			prisonerRepository.updatePrisonerPhotoId(imgId, prisonerId);
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Image updated successfully", imgId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Invalid data format"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('NETWORKMAN')")
	@PutMapping(value = "/prisons/upload-photos/{prisonId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> updatePrisonPhoto(@PathVariable(value = "prisonId") Long prisonId,
			@RequestParam("file") MultipartFile file) {
		try {
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

			// File Name
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-z");
			String strDate = formatter.format(date);
			String id = Long.toString(prisonId);
			String fileName = "prisonerPhoto-" + id + "-" + strDate + ".png";
			//
			imageRepository.deletePrisonPhoto(prisonId);
			ImageModel img = new ImageModel(null, fileName, fileType, compressBytes(file.getBytes()));
			imageRepository.save(img);
			Long imgId = img.getId();
			prisonRepository.updatePrisonPhotoId(imgId, prisonId);
			return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Image updated successfully", imgId),
					HttpStatus.OK);
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
}
