package com.app.server.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.server.model.Prison;
import com.app.server.model.Prisoner;

@Repository
public interface PrisonerRepository extends JpaRepository<Prisoner, Long> {

	Prisoner findByPrisonerId(Long prisonerId);

	List<Prisoner> findAllByOrderByCreatedTimestampAsc();

	List<Prisoner> findByPrisonOrderByCreatedTimestampAsc(Prison prisoner);

	Boolean existsByIdentifierId(String identifierId);

	Boolean existsByBraceletId(String braceletId);

	@Query("SELECT P FROM prisoner P, prison PR WHERE P.prison = PR.prisonId AND P.prisonerId = ?1 AND P.prison = ?2")
	Optional<Prisoner> findByUserLoggedPrison(Long prisonerId, Prison prison);

	@Query("SELECT P FROM prisoner P, prison PR WHERE P.alertOff = false AND P.prison = PR.prisonId AND P.prison = ?1")
	Optional<List<Prisoner>> findByAlertPrisons(Prison prison);

	@Transactional
	@Modifying
	@Query("UPDATE prisoner SET cell = ?1, braceletId = ?2, maxHB = ?3, minHB = ?4, alertOff = ?5 where prisoner_id = ?6")
	void updatePrisonerAsGuard(String cell, String braceletId, int maxHB, int minHB, boolean alertOff, Long prisonerId);

	@Transactional
	@Modifying
	@Query("UPDATE prisoner SET identifier_id = ?1, birth_Date = ?2, nationality = ?3, name = ?4, contact = ?5, alternative_contact = ?6, cell = ?7, threat_level = ?8, prison_id = ?9, bracelet_id = ?10, maxHB = ?11, minHB = ?12, alertOff = ?13 where prisoner_id = ?14")
	void updatePrisonerAsManager(String identifierId, Date birthDate, String nationality, String name, String contact,
			String alternativeContact, String cell, int threatLevel, Long prisonId, String BraceletId, int maxHB,
			int minHB, boolean alertOff, Long prisonerId);

	@Transactional
	@Modifying
	@Query("UPDATE prisoner SET photo = ?1 WHERE prisoner_id = ?2")
	void updatePrisonerPhoto(String photo, Long prisonerId);

	@Transactional
	@Modifying
	@Query("UPDATE prisoner SET photoId = ?1 WHERE prisoner_id = ?2")
	void updatePrisonerPhotoId(Long photoId, Long prisonerId);

}