package com.solvd.bankatmsimulator.service;

import com.solvd.bankatmsimulator.domain.entity.PaymentCard;
import java.util.List;

public interface IPaymentCardService {

    PaymentCard register(PaymentCard card);

    PaymentCard update(PaymentCard card);

    PaymentCard getById(long id);

    List<PaymentCard> getAll();

    void delete(long id);

    PaymentCard getByCardNumber(String cardNumber);

    List<PaymentCard> getByAccountId(long accountId);

    List<PaymentCard> getByStatus(PaymentCard.CardStatus status);

    List<PaymentCard> getExpiredCards();
}
