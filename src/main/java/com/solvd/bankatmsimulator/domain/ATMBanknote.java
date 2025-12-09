package com.solvd.bankatmsimulator.domain;

import java.math.BigDecimal;

public class ATMBanknote {

    private Long id;
    private Long atmId;
    private String currency;
    private BigDecimal denomination;
    private Integer quantity;

    public ATMBanknote() {
    }

    public ATMBanknote(Long id, String currency) {
        this.id = id;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAtmId() {
        return atmId;
    }

    public void setAtmId(Long atmId) {
        this.atmId = atmId;
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

