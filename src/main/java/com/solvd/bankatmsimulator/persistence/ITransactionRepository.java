package com.solvd.bankatmsimulator.persistence;

import com.solvd.bankatmsimulator.domain.entity.Transaction;

import java.util.List;

public interface ITransactionRepository extends IRepository<Transaction> {
    
    List<Transaction> findByAccountId(Long accountId);
    
    List<Transaction> findByFromAccountId(Long fromAccountId);
    
    List<Transaction> findByToAccountId(Long toAccountId);
    
    List<Transaction> findByType(Transaction.TransactionType type);
    
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
}

