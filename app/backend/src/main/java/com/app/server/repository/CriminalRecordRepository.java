package com.app.server.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.server.model.CriminalRecord;

@Repository
public interface CriminalRecordRepository extends JpaRepository<CriminalRecord, Long> {

	CriminalRecord findByCriminalRecordId(Long criminalRecordId);

	List<CriminalRecord> findAllByOrderByCreatedTimestampAsc();

	@Transactional
	@Modifying
	@Query("UPDATE criminalRecord SET name = ?1, description = ?2, emission_date = ?3 WHERE criminal_record_id = ?4 AND prisoner_id = ?5")
	void updateCriminalRecordAsManager(String name, String description, Date emissionDate, Long criminalRecordId,
			Long prisonerId);

}
