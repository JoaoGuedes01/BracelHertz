package com.app.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.server.model.Prison;
import com.app.server.model.PrisonerLog;

@Repository
public interface PrisonerLogRepository extends JpaRepository<PrisonerLog, Long> {

	PrisonerLog findByPrisonerLogId(Long prisonerId);

	List<PrisonerLog> findAllByOrderByLogTimestampDesc();
	
	@Query("SELECT PL FROM prisonerLog PL, prisoner P WHERE PL.prisoner = P.prisonerId AND P.prisonerId = ?1 ORDER BY PL.logTimestamp DESC")
	Optional<List<PrisonerLog>> findByPrisonerLogPrisonerId(Long prisonerId);

	@Query("SELECT PL FROM prisonerLog PL, prisoner P WHERE PL.prisoner = P.prisonerId AND P.prison = ?1 ORDER BY PL.logTimestamp DESC")
	Optional<List<PrisonerLog>> findByPrisonerLogPrisonId(Prison prison);

}
