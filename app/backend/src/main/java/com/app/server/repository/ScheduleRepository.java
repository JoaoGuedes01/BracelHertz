package com.app.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.server.model.Schedule;
import com.app.server.model.User;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	Schedule findByScheduleId(Long scheduleId);

	List<Schedule> findByUser(User user);

	@Transactional
	@Modifying
	@Query("DELETE From schedule WHERE schedule_id = ?1 AND user_id = ?2")
	void deleteSchedule(Long scheduleId, Long userId);

}
