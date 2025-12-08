package com.solvd.bankatmsimulator.domain;

import java.math.BigDecimal;

public class WithdrawalBanknote {

    private Long id;
    private Long withdrawalId;
    private BigDecimal denomination;
    private Integer quantity;

    public WithdrawalBanknote() {
    }

    public WithdrawalBanknote(Long id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(Long withdrawalId) {
        this.withdrawalId = withdrawalId;
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
        return "WithdrawalBanknote{" +
                "id=" + id +
                ", withdrawalId=" + withdrawalId +
                ", denomination=" + denomination +
                ", quantity=" + quantity +
                '}';
    }
}

