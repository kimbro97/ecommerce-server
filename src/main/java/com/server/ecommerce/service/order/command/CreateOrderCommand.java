package com.server.ecommerce.service.order.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrderCommand {
    private final Long userId;
    private final List<Long> cartIds;
}