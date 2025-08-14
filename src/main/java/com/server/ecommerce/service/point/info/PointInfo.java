package com.server.ecommerce.service.point.info;

import com.server.ecommerce.domain.point.Point;

import lombok.Getter;

@Getter
public class PointInfo {
    
    private final Long pointId;
    private final Long userId;
    private final Long balance;
    
    public PointInfo(Long pointId, Long userId, Long balance) {
        this.pointId = pointId;
        this.userId = userId;
        this.balance = balance;
    }
    
    public static PointInfo from(Point point) {
        return new PointInfo(point.getId(), point.getUserId(), point.getBalance());
    }
}