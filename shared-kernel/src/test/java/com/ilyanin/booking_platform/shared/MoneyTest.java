package com.ilyanin.booking_platform.shared;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.jupiter.api.Test;

public class MoneyTest {
    
    @Test
    void shouldReturnValidMoney() {
        Money money = new Money(BigDecimal.valueOf(100), Currency.getInstance("USD"));
        
        assertThat(money.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(money.currency()).isEqualTo(Currency.getInstance("USD"));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        assertThatThrownBy(() -> new Money(new BigDecimal(-55), Currency.getInstance("USD")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAddCorrectly() {
        Money money = new Money(BigDecimal.valueOf(100), Currency.getInstance("USD"));
        Money other = new Money(BigDecimal.valueOf(1200), Currency.getInstance("USD"));
        
        Money result = money.add(other);
        
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(1300));
        assertThat(result.currency()).isEqualTo(Currency.getInstance("USD"));
    }

    @Test
    void shouldThrowExceptionWhenCurrenicesAreDifferent() {
        Money eur = new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR"));
        Money usd = new Money(BigDecimal.valueOf(1200), Currency.getInstance("USD"));

        assertThatThrownBy(() -> eur.add(usd))
            .isInstanceOf(IllegalArgumentException.class);        
    
        assertThatThrownBy(() -> eur.subtract(usd))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldSubtractCorrectly() {
        Money money = new Money(BigDecimal.valueOf(1200), Currency.getInstance("USD"));
        Money other = new Money(BigDecimal.valueOf(100), Currency.getInstance("USD"));
        
        Money result = money.subtract(other);
        
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(1100));
        assertThat(result.currency()).isEqualTo(Currency.getInstance("USD"));
    }

    @Test
    void shouldThrowExceptionWhenSubstrahendIsMoreThanAmount() {
        Money amount = new Money(BigDecimal.valueOf(100), Currency.getInstance("USD"));
        Money substrahend = new Money(BigDecimal.valueOf(1200), Currency.getInstance("USD"));
        
        assertThatThrownBy(
            () -> amount.subtract(substrahend)
        )
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenMultiplyByNegative() {
        Money amount = new Money(BigDecimal.valueOf(100), Currency.getInstance("USD"));
        var factor = BigDecimal.valueOf(-3);
        assertThatThrownBy(
            () -> amount.multiply(factor)
        )
            .isInstanceOf(IllegalArgumentException.class);
    }
}
