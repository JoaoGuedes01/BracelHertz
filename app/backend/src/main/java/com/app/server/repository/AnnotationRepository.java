package com.app.server.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.server.model.Annotation;
import com.app.server.model.Prison;
import com.app.server.model.Prisoner;
import com.app.server.model.User;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {

	Annotation findByAnnotationId(Long annotationId);

	List<Annotation> findAllByOrderByCreatedTimestampAsc();

	@Query("SELECT A.annotationId, A.createdBy, A.prisonDest, A.title, A.description, A.createdTimestamp, A.lastUpdatedTimestamp FROM annotation A ORDER BY A.createdTimestamp DESC")
	Optional<List<Annotation>> findByPrisonsAnnotationsNet();

	@Query("SELECT A.annotationId, A.createdBy, A.prisonDest, A.title, A.description, A.createdTimestamp, A.lastUpdatedTimestamp FROM annotation A WHERE A.prisonDest = ?1 ORDER BY A.createdTimestamp DESC")
	Optional<List<Annotation>> findByPrisonAnnotations(Prison prisonDest);

	@Query("SELECT A.annotationId, A.createdBy, A.prisonerDest, A.title, A.description, A.createdTimestamp, A.lastUpdatedTimestamp FROM annotation A, prisoner P WHERE A.prisonerDest = P.prisonerId AND P.prison = ?1 ORDER BY A.createdTimestamp DESC")
	Optional<List<Annotation>> findByPrisonersAnnotations(Prison prison);

	@Query("SELECT A.annotationId, A.createdBy, A.prisonerDest, A.title, A.description, A.createdTimestamp, A.lastUpdatedTimestamp FROM annotation A ORDER BY A.createdTimestamp DESC")
	Optional<List<Annotation>> findByPrisonersAnnotationsNet();

	@Query("SELECT A.annotationId, A.createdBy, A.prisonerDest, A.title, A.description, A.createdTimestamp, A.lastUpdatedTimestamp FROM annotation A WHERE A.prisonerDest = ?1 ORDER BY A.createdTimestamp DESC")
	Optional<List<Annotation>> findByPrisonerAnnotations(Prisoner prisonerDest);

	@Query("SELECT A.annotationId, A.createdBy, A.userDest, A.title, A.description, A.createdTimestamp, A.lastUpdatedTimestamp FROM annotation A WHERE A.userDest = ?1 ORDER BY A.createdTimestamp DESC")
	Optional<List<Annotation>> findByUserAnnotations(User userDest);

	@Query("SELECT A.annotationId, A.createdBy, A.annotationDest, A.title, A.description, A.createdTimestamp, A.lastUpdatedTimestamp FROM annotation A WHERE A.annotationDest = ?1 ORDER BY A.createdTimestamp ASC")
	Optional<List<Annotation>> findByCommentsAnnotation(Annotation annotationDest);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO annotation SET created_by = ?1, prison_dest_id = ?2, title = ?3, description = ?4", nativeQuery = true)
	void createPrisonAnnotation(Long createdBy, Long prisonDestId, String title, String description);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO annotation SET created_by = ?1, prisoner_dest_id = ?2, title = ?3, description = ?4", nativeQuery = true)
	void createPrisonerAnnotation(Long createdBy, Long prisonerDestId, String title, String description);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO annotation SET created_by = ?1, user_dest_id = ?2, title = ?3, description = ?4", nativeQuery = true)
	void createUserAnnotation(Long createdBy, Long userDestId, String title, String description);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO annotation SET created_by = ?1, annotation_dest_id = ?2, title = ?3, description = ?4", nativeQuery = true)
	void createCommentAnnotation(Long createdBy, Long annotationDestId, String title, String description);

	@Transactional
	@Modifying
	@Query(value = "Update annotation SET title = ?1, description = ?2, last_updated_timestamp = ?3 WHERE annotation_id = ?4", nativeQuery = true)
	void updateAnnotation(String title, String description, LocalDateTime lasUpdatedTimestamp, Long annotationId);

}
