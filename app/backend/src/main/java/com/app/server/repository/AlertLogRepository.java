package com.app.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.server.model.AlertLog;
import com.app.server.model.Prison;
import com.app.server.model.Prisoner;

@Repository
public interface AlertLogRepository extends JpaRepository<AlertLog, Long> {

	AlertLog findByAlertLogId(Long alertLogId);

	List<AlertLog> findAllByOrderByCreatedTimestampDesc();

	@Query("SELECT AL FROM alertLog AL, prisoner P WHERE AL.prisoner = P.prisonerId AND P.prison = ?1 ORDER BY AL.createdTimestamp DESC")
	List<AlertLog> findAlertLogsByPrison(Prison prison);

	@Query("SELECT AL FROM alertLog AL WHERE AL.prisoner = ?1 ORDER BY AL.createdTimestamp DESC")
	List<AlertLog> findAlertLogsByPrisoner(Prisoner prisoner);

	@Query(value = "SELECT COUNT(DISTINCT AL.prisoner_id) FROM alert_log AL WHERE DATE(AL.created_timestamp) BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE()", nativeQuery = true)
	int totalDistinctAlertsInLastMonth();

	@Query(value = "SELECT COUNT(DISTINCT AL.prisoner_id) FROM alert_log AL, prisoner P WHERE AL.prisoner_id = P.prisoner_id AND P.prison_id = ?1 AND DATE(AL.created_timestamp) BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE();", nativeQuery = true)
	int totalDistinctAlertsInLastMonthByPrisonId(Long prisonId);

}