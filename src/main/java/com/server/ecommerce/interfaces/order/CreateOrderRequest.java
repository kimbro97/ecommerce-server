package com.server.ecommerce.interfaces.order;

import java.util.List;

import com.server.ecommerce.service.order.command.CreateOrderCommand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private List<Long> cartIds;

    public CreateOrderCommand toCommand(Long userId) {
        return new CreateOrderCommand(userId, cartIds);
    }
}