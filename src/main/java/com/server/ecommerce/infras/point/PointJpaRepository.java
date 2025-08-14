package com.server.ecommerce.infras.point;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.server.ecommerce.domain.point.Point;

import jakarta.persistence.LockModeType;

public interface PointJpaRepository extends JpaRepository<Point, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Point p WHERE p.userId = :userId")
    Optional<Point> findByUserIdWithLock(@Param("userId") Long userId);

    Optional<Point> findByUserId(Long userId);
}