package com.server.ecommerce.service.point.command;

import lombok.Getter;

@Getter
public class UsePointCommand {
    
    private final Long userId;
    private final Long amount;
    
    public UsePointCommand(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }
}