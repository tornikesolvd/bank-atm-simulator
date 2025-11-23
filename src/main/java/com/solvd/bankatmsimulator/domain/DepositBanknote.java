package com.solvd.bankatmsimulator.domain;

import java.math.BigDecimal;

public class DepositBanknote {

    private Long id;
    private Long depositId;
    private BigDecimal denomination;
    private Integer quantity;

    public DepositBanknote() {
    }

    public DepositBanknote(Long depositId,Integer quantity) {
        this.depositId = depositId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepositId() {
        return depositId;
    }

    public void setDepositId(Long depositId) {
        this.depositId = depositId;
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
        return "DepositBanknote{" +
                "id=" + id +
                ", depositId=" + depositId +
                ", denomination=" + denomination +
                ", quantity=" + quantity +
                '}';
    }
}

