package com.server.ecommerce.interfaces.point;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.service.point.PointService;
import com.server.ecommerce.service.point.info.PointInfo;
import com.server.ecommerce.support.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/users/{userId}/points/charge")
    public ResponseEntity<ApiResponse<PointInfo>> chargePoint(
        @PathVariable Long userId,
        @RequestBody ChargePointRequest request) {
        return ApiResponse.OK(pointService.chargePoint(request.toCommand(userId)));
    }

    @PostMapping("/users/{userId}/points/use")
    public ResponseEntity<ApiResponse<PointInfo>> usePoint(
        @PathVariable Long userId,
        @RequestBody UsePointRequest request) {
        return ApiResponse.OK(pointService.usePoint(request.toCommand(userId)));
    }

    @GetMapping("/users/{userId}/points")
    public ResponseEntity<ApiResponse<PointInfo>> getPointBalance(@PathVariable Long userId) {
        return ApiResponse.OK(pointService.getPointBalance(userId));
    }
}