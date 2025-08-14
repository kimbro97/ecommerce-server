package com.server.ecommerce.interfaces.point;

import com.server.ecommerce.service.point.command.ChargePointCommand;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChargePointRequest {
    
    private Long amount;
    
    public ChargePointCommand toCommand(Long userId) {
        return new ChargePointCommand(userId, amount);
    }
}