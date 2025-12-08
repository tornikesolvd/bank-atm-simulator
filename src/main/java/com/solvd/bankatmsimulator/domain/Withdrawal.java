package com.solvd.bankatmsimulator.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Withdrawal {

    public static final BigDecimal MIN_AMOUNT = new BigDecimal("5.00");

    private Long id;
    private Long accountId;
    private Long transactionId;
    private Long atmId;
    private String currency;
    private BigDecimal totalAmount;
    private LocalDateTime processedAt;
    private List<WithdrawalBanknote> banknotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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

    public List<WithdrawalBanknote> getBanknotes() {
        if (banknotes == null) {
            banknotes = new ArrayList<>();
        }
        return banknotes;
    }

    public void setBanknotes(List<WithdrawalBanknote> banknotes) {
        this.banknotes = banknotes;
    }

    public void addBanknote(WithdrawalBanknote banknote) {
        getBanknotes().add(banknote);
    }

    public void removeBanknote(WithdrawalBanknote banknote) {
        getBanknotes().remove(banknote);
    }

    @Override
    public String toString() {
        return "Withdrawal{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", transactionId=" + transactionId +
                ", atmId=" + atmId +
                ", currency='" + currency + '\'' +
                ", totalAmount=" + totalAmount +
                ", processedAt=" + processedAt +
                ", banknotesCount=" + (banknotes != null ? banknotes.size() : 0) +
                '}';
    }
}

