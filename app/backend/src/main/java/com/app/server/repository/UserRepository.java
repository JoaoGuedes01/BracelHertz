package com.app.server.repository;

import com.app.server.model.Prison;
import com.app.server.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserId(Long userId);
	
	User findByPhotoId(Long photoId);
	
	@Query("SELECT U FROM user U WHERE username = ?1")
	User getUserByUsername(String username);
	
	List<User> findAllByOrderByCreatedTimestampAsc();
	
	List<User> findAllByPrison(Prison prison);
	
	Boolean existsByUsername(String username);

	@Transactional
	@Modifying
	@Query("UPDATE user SET photo = ?1 WHERE user_id = ?2")
	void updateUserPhoto(String photo, Long userId);

	@Transactional
	@Modifying
	@Query("UPDATE user SET password = ?1 WHERE user_id = ?2")
	void updateUserPassword(String password, Long userId);
	
	@Transactional
	@Modifying
	@Query("UPDATE user SET password = ?1, passwordToken = ?2 WHERE user_id = ?3")
	void updateUserPasswordWithToken(String password, String passwordToken, Long userId);
	
	@Transactional
	@Modifying
	@Query("UPDATE user SET passwordToken = ?1 WHERE user_id = ?2")
	void updateUserPasswordToken(String newPasswordToken, Long userId);

	@Transactional
	@Modifying
	@Query("UPDATE user SET email = ?1, contact = ?2 WHERE user_id = ?3")
	void updateUserAsGuard(String email, String contact, Long userId);

	@Transactional
	@Modifying
	@Query("UPDATE user SET birth_date = ?1, nationality = ?2, address = ?3, location = ?4, name = ?5, contact = ?6, email = ?7, prison_id = ?8 WHERE user_id = ?9")
	void updateUserAsManager(Date birthDate, String nationality, String address, String location, String name,
			String contact, String email, Long prisonId, Long userId);
	
	@Transactional
	@Modifying
	@Query("UPDATE user SET photoId = ?1 WHERE user_id = ?2")
	void updateUserPhotoId(Long photoId, Long userId);

	Optional<User> findByUsername(String username);

}