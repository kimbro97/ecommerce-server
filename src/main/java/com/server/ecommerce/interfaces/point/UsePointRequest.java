package com.server.ecommerce.interfaces.point;

import com.server.ecommerce.service.point.command.UsePointCommand;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsePointRequest {
    
    private Long amount;
    
    public UsePointCommand toCommand(Long userId) {
        return new UsePointCommand(userId, amount);
    }
}