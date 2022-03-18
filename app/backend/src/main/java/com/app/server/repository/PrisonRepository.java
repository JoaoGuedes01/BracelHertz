package com.app.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.server.model.Prison;

@Repository
public interface PrisonRepository extends JpaRepository<Prison, Long> {

	Prison findByPrisonId(Long prisonId);

	List<Prison> findAllByOrderByPrisonIdAsc();

	Boolean existsByPrisonId(Long PrisonId);

	@Transactional
	@Modifying
	@Query("UPDATE prison SET photo = ?1 WHERE prison_id = ?2")
	void updatePrisonPhoto(String photo, Long prisonId);
	
	@Transactional
	@Modifying
	@Query("UPDATE prison SET photoId = ?1 WHERE prison_id = ?2")
	void updatePrisonPhotoId(Long photoId, Long prisonId);
}
