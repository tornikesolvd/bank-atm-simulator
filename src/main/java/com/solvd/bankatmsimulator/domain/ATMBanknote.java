package com.solvd.bankatmsimulator.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ATMBanknote {

    private Long id;
    private String currency;
    private BigDecimal denomination;
    private Integer quantity;

    public ATMBanknote() {
    }

    public ATMBanknote(Long id, String currency) {
        this.id=id;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ATMBanknote{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                ", denomination=" + denomination +
                ", quantity=" + quantity +
                '}';
    }
}

