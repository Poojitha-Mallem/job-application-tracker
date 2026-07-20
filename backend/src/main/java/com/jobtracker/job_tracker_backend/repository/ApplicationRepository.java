package com.jobtracker.job_tracker_backend.repository;

import com.jobtracker.job_tracker_backend.entity.Application;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findByUserId(Long userId, Pageable pageable);

    Page<Application> findByUserIdAndStatus(Long userId, Application.Status status, Pageable pageable);

    @Query("""
        SELECT a FROM Application a
        WHERE a.user.id = :userId
        AND (CAST(:companyName AS string) IS NULL OR LOWER(a.companyName) LIKE LOWER(CONCAT('%', CAST(:companyName AS string), '%')))
        AND (:status IS NULL OR a.status = :status)
        """)
    Page<Application> searchApplications(
        @Param("userId") Long userId,
        @Param("companyName") String companyName,
        @Param("status") Application.Status status,
        Pageable pageable
    );

    @Query("""
        SELECT a FROM Application a
        WHERE a.updatedAt < :cutoff
        AND a.status NOT IN ('OFFER', 'REJECTED', 'WITHDRAWN')
        """)
    List<Application> findStaleApplications(@Param("cutoff") java.time.LocalDateTime cutoff);

    long countByUserId(Long userId);

    @Query("""
        SELECT a.status, COUNT(a) FROM Application a
        WHERE a.user.id = :userId
        GROUP BY a.status
        """)
    List<Object[]> countGroupedByStatus(@Param("userId") Long userId);

    long countByUserIdAndStatus(Long userId, Application.Status status);
}