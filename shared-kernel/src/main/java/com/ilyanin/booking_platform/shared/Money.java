package com.ilyanin.booking_platform.shared;

import java.math.BigDecimal;
import java.util.Currency;

public record Money(
    BigDecimal amount,
    Currency currency
) {

    public Money {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException(
                "Amount cannot be negative: " + amount
            );
        }
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "Cannot add money with different currencies: " 
                + currency + " and " + other.currency
            );
        }
        return new Money(amount.add(other.amount), currency);
    }

    public Money multiply(BigDecimal factor) {
        if (factor.signum() < 0) {
            throw new IllegalArgumentException(
                "Factor cannot be negative: " + factor
            );
        }
        return new Money(amount.multiply(factor), currency);
    }

    public Money subtract(Money substrahend) {
        if (!currency.equals(substrahend.currency)) {
            throw new IllegalArgumentException(
                "Cannot subtract money with different currencies: " 
                + currency + " and " + substrahend.currency
            );
        }

        BigDecimal result = amount.subtract(substrahend.amount);
        if (result.signum() < 0) {
            throw new IllegalArgumentException("Amount cannot be negative: " + result);
        } 
        return new Money(result, currency);
    }

    public Money defaultMoney() {
        return new Money(BigDecimal.valueOf(0), Currency.getInstance("USD"));
    }
}
