package com.solvd.bankatmsimulator.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentCard {

    private Long id;
    private String cardNumber;
    private CardType cardType;
    private CardStatus status;
    private String pinHash;
    private LocalDate expiryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    public boolean isActive() {
        return status == CardStatus.ACTIVE && !isExpired();
    }

    @Override
    public String toString() {
        return "PaymentCard{" +
                "id=" + id +
                ", cardNumber='" + maskCardNumber(cardNumber) + '\'' +
                ", cardType=" + cardType +
                ", status=" + status +
                ", expiryDate=" + expiryDate +
                ", created at=" + createdAt +
                ", updated at=" + updatedAt +
                '}';
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }

    public enum CardType {
        DEBIT,
        CREDIT,
        PREPAID
    }

    public enum CardStatus {
        ACTIVE,
        BLOCKED,
        EXPIRED,
        CANCELLED
    }

    public enum CardIssuer {
        VISA,
        MASTERCARD,
        AMERICAN_EXPRESS,
        DISCOVER,
        UNKNOWN
    }
}

