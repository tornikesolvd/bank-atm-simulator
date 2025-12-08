package com.solvd.bankatmsimulator.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Deposit {

    public static final BigDecimal MIN_AMOUNT = new BigDecimal("1.00");

    private Long id;
    private Long transactionId;
    private Long atmId;
    private String currency;
    private BigDecimal totalAmount;
    private LocalDateTime processedAt;
    private List<DepositBanknote> banknotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public List<DepositBanknote> getBanknotes() {
        if (banknotes == null) {
            banknotes = new ArrayList<>();
        }
        return banknotes;
    }

    public void setBanknotes(List<DepositBanknote> banknotes) {
        this.banknotes = banknotes;
    }

    public void addBanknote(DepositBanknote banknote) {
        getBanknotes().add(banknote);
    }

    public void removeBanknote(DepositBanknote banknote) {
        getBanknotes().remove(banknote);
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "id=" + id +
                ", transactionId=" + transactionId +
                ", atmId=" + atmId +
                ", currency='" + currency + '\'' +
                ", totalAmount=" + totalAmount +
                ", processedAt=" + processedAt +
                ", banknotesCount=" + (banknotes != null ? banknotes.size() : 0) +
                '}';
    }
}

