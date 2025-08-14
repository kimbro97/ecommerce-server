package com.server.ecommerce.domain.point;

import static com.server.ecommerce.support.exception.BusinessError.*;

import com.server.ecommerce.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

    @Id
    @Column(name = "point_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long balance;

    public Point(Long userId, Long balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public static Point create(Long userId) {
        return new Point(userId, 0L);
    }

    public void charge(Long amount) {
        if (amount <= 0) {
            throw POINT_INVALID_CHARGE_AMOUNT.exception();
        }
        this.balance += amount;
    }

    public void use(Long amount) {
        if (amount <= 0) {
            throw POINT_INVALID_USE_AMOUNT.exception();
        }
        if (this.balance < amount) {
            throw POINT_INSUFFICIENT_BALANCE.exception();
        }
        this.balance -= amount;
    }
}