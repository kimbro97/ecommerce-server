package com.server.ecommerce.service.point;

import static com.server.ecommerce.support.exception.BusinessError.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.point.Point;
import com.server.ecommerce.domain.point.PointRepository;
import com.server.ecommerce.service.point.command.ChargePointCommand;
import com.server.ecommerce.service.point.command.UsePointCommand;
import com.server.ecommerce.service.point.info.PointInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public PointInfo chargePoint(ChargePointCommand command) {
        Point point = pointRepository.findByUserIdWithLock(command.getUserId())
            .orElseGet(() -> pointRepository.save(Point.create(command.getUserId())));

        point.charge(command.getAmount());

        return PointInfo.from(point);
    }

    @Transactional
    public PointInfo usePoint(UsePointCommand command) {
        Point point = pointRepository.findByUserIdWithLock(command.getUserId())
            .orElseThrow(POINT_ACCOUNT_NOT_FOUND::exception);

        point.use(command.getAmount());
        point = pointRepository.save(point);

        return PointInfo.from(point);
    }

	@Transactional
    public PointInfo getPointBalance(Long userId) {
        Point point = pointRepository.findByUserId(userId)
            .orElseGet(() -> pointRepository.save(Point.create(userId)));

        return PointInfo.from(point);
    }
}
