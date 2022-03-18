package com.app.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.server.model.Prison;
import com.app.server.model.UserLog;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {

	UserLog findByUserLogId(Long userLogId);
	
	List<UserLog> findAllByOrderByLogTimestampDesc();
	
	@Query("SELECT UL FROM userLog UL, user U WHERE UL.user = U.userId AND U.userId = ?1 ORDER BY UL.logTimestamp DESC")
	Optional<List<UserLog>> findByUserLogUserId(Long userId);

	@Query("SELECT UL FROM userLog UL, user U WHERE UL.user = U.userId AND U.prison = ?1 ORDER BY UL.logTimestamp DESC")
	Optional<List<UserLog>> findByUserLogPrisonId(Prison prison);
}
