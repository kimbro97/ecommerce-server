package com.server.ecommerce.domain.point;

import java.util.Optional;

public interface PointRepository {
    
    Point save(Point point);
    
    Optional<Point> findByUserIdWithLock(Long userId);
    
    Optional<Point> findByUserId(Long userId);
}