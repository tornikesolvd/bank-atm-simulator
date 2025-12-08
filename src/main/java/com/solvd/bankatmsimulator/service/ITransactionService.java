package com.solvd.bankatmsimulator.service;

import com.solvd.bankatmsimulator.domain.Transaction;
import java.util.List;

public interface ITransactionService {

    Transaction register(Transaction transaction);

    Transaction update(Transaction transaction);

    Transaction getById(long id);

    List<Transaction> getAll();

    void delete(long id);

    List<Transaction> getByAccountId(long accountId);

    List<Transaction> getByFromAccountId(long accountId);

    List<Transaction> getByToAccountId(long accountId);

    List<Transaction> getByType(Transaction.TransactionType type);

    List<Transaction> getByStatus(Transaction.TransactionStatus status);
}
