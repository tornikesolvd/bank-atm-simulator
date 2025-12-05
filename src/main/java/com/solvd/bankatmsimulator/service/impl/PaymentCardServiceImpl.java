package com.solvd.bankatmsimulator.service.impl;

import com.mysql.cj.util.StringUtils;
import com.solvd.bankatmsimulator.domain.entity.PaymentCard;
import com.solvd.bankatmsimulator.domain.exception.PaymentCardException;
import com.solvd.bankatmsimulator.persistence.IPaymentCardRepository;
import com.solvd.bankatmsimulator.persistence.impl.PaymentCardRepositoryImpl;
import com.solvd.bankatmsimulator.service.IPaymentCardService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentCardServiceImpl implements IPaymentCardService {

    private final IPaymentCardRepository repository;

    public PaymentCardServiceImpl() {
        this.repository = new PaymentCardRepositoryImpl();
    }

    @Override
    public PaymentCard register(PaymentCard card) {
        validateForCreate(card);
        return repository.create(card);
    }

    @Override
    public PaymentCard update(PaymentCard card) {
        validateForUpdate(card);
        return repository.update(card);
    }

    @Override
    public PaymentCard getById(long id) {
        if (id <= 0) {
            throw PaymentCardException.invalidId();
        }
        return repository.findById(id)
                .orElseThrow(() -> PaymentCardException.notFound(id));
    }

    @Override
    public List<PaymentCard> getAll() {
        List<PaymentCard> cards = repository.findAll();
        if (cards == null || cards.isEmpty()) {
            throw PaymentCardException.emptyList();
        }
        return cards;
    }

    @Override
    public void delete(long id) {
        if (id <= 0) {
            throw PaymentCardException.invalidId();
        }
        if (repository.findById(id).isEmpty()) {
            throw PaymentCardException.notFound(id);
        }
        repository.delete(id);
    }

    @Override
    public PaymentCard getByCardNumber(String cardNumber) {
        if (StringUtils.isNullOrEmpty(cardNumber)) {
            throw PaymentCardException.invalidCardNumber();
        }
        return repository.findByCardNumber(cardNumber)
                .orElseThrow(PaymentCardException::cardIsNull);
    }

    @Override
    public List<PaymentCard> getByAccountId(long accountId) {
        if (accountId <= 0) {
            throw PaymentCardException.invalidId();
        }
        List<PaymentCard> cards = repository.findByAccountId(accountId);
        if (cards.isEmpty()) {
            throw PaymentCardException.emptyList();
        }
        return cards;
    }

    @Override
    public List<PaymentCard> getByStatus(PaymentCard.CardStatus status) {
        if (status == null) {
            throw PaymentCardException.invalidStatus();
        }
        List<PaymentCard> cards = repository.findByStatus(status);
        if (cards.isEmpty()) {
            throw PaymentCardException.emptyList();
        }
        return cards;
    }

    @Override
    public List<PaymentCard> getExpiredCards() {
        List<PaymentCard> cards = repository.findExpiredCards();
        if (cards.isEmpty()) {
            throw PaymentCardException.emptyList();
        }
        return cards;
    }

    private void validateForCreate(PaymentCard card) {
        if (card == null) {
            throw PaymentCardException.cardIsNull();
        }
        if (card.getId() != null) {
            throw PaymentCardException.invalidId();
        }
        if (StringUtils.isNullOrEmpty(card.getCardNumber())) {
            throw PaymentCardException.invalidCardNumber();
        }
        if (card.getCardType() == null) {
            throw PaymentCardException.invalidCardType();
        }
        if (card.getStatus() == null) {
            throw PaymentCardException.invalidStatus();
        }
        if (StringUtils.isNullOrEmpty(card.getPinHash())) {
            throw PaymentCardException.invalidPinHash();
        }
        if (card.getExpiryDate() == null || card.getExpiryDate().isBefore(LocalDate.now())) {
            throw PaymentCardException.invalidExpiryDate();
        }
        if (card.getCreatedAt() == null) {
            card.setCreatedAt(LocalDateTime.now());
        }
        if (card.getUpdatedAt() == null) {
            card.setUpdatedAt(LocalDateTime.now());
        }
    }

    private void validateForUpdate(PaymentCard card) {
        if (card == null) {
            throw PaymentCardException.cardIsNull();
        }
        if (card.getId() == null || card.getId() <= 0) {
            throw PaymentCardException.invalidId();
        }
        if (StringUtils.isNullOrEmpty(card.getCardNumber())) {
            throw PaymentCardException.invalidCardNumber();
        }
        if (card.getCardType() == null) {
            throw PaymentCardException.invalidCardType();
        }
        if (card.getStatus() == null) {
            throw PaymentCardException.invalidStatus();
        }
        if (StringUtils.isNullOrEmpty(card.getPinHash())) {
            throw PaymentCardException.invalidPinHash();
        }
        if (card.getExpiryDate() == null || card.getExpiryDate().isBefore(LocalDate.now())) {
            throw PaymentCardException.invalidExpiryDate();
        }
        if (card.getUpdatedAt() == null) {
            card.setUpdatedAt(LocalDateTime.now());
        }
    }
}
