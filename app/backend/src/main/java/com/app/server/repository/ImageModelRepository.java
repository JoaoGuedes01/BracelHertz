package com.app.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.app.server.model.ImageModel;

public interface ImageModelRepository extends JpaRepository<ImageModel, Long> {
	Optional<ImageModel> findByName(String name);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM image_table WHERE id=(SELECT photo_id FROM user WHERE user_id = ?1)", nativeQuery = true)
	void deleteUserPhoto(Long userId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM image_table WHERE id=(SELECT photo_id FROM prison WHERE prison_id = ?1)", nativeQuery = true)
	void deletePrisonPhoto(Long prisonId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM image_table WHERE id=(SELECT photo_id FROM prisoner WHERE prisoner_id = ?1)", nativeQuery = true)
	void deletePrisonerPhoto(Long prisonerId);
}