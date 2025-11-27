package com.solvd.bankatmsimulator.persistence;

import com.solvd.bankatmsimulator.domain.PaymentCard;

import java.util.List;
import java.util.Optional;

public interface IPaymentCardRepository extends IRepository<PaymentCard> {
    
    Optional<PaymentCard> findByCardNumber(String cardNumber);
    
    List<PaymentCard> findByAccountId(Long accountId);
    
    List<PaymentCard> findByStatus(PaymentCard.CardStatus status);
    
    List<PaymentCard> findExpiredCards();
}

