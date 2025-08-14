package com.server.ecommerce.service.point.command;

import lombok.Getter;

@Getter
public class ChargePointCommand {
    
    private final Long userId;
    private final Long amount;
    
    public ChargePointCommand(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }
}