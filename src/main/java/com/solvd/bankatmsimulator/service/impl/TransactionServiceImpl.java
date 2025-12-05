package com.solvd.bankatmsimulator.service.impl;

import com.mysql.cj.util.StringUtils;
import com.solvd.bankatmsimulator.domain.entity.Transaction;
import com.solvd.bankatmsimulator.domain.exception.TransactionException;
import com.solvd.bankatmsimulator.persistence.ITransactionRepository;
import com.solvd.bankatmsimulator.persistence.impl.TransactionRepositoryImpl;
import com.solvd.bankatmsimulator.service.ITransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository repository;

    public TransactionServiceImpl() {
        this.repository = new TransactionRepositoryImpl();
    }

    @Override
    public Transaction register(Transaction transaction) {
        validateForCreate(transaction);
        return repository.create(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        validateForUpdate(transaction);
        return repository.update(transaction);
    }

    @Override
    public Transaction getById(long id) {
        if (id <= 0) {
            throw TransactionException.invalidId();
        }
        return repository.findById(id)
                .orElseThrow(() -> TransactionException.notFound(id));
    }

    @Override
    public List<Transaction> getAll() {
        List<Transaction> transactions = repository.findAll();
        if (transactions == null || transactions.isEmpty()) {
            throw TransactionException.emptyList();
        }
        return transactions;
    }

    @Override
    public void delete(long id) {
        if (id <= 0) {
            throw TransactionException.invalidId();
        }
        if (repository.findById(id).isEmpty()) {
            throw TransactionException.notFound(id);
        }
        repository.delete(id);
    }

    @Override
    public List<Transaction> getByAccountId(long accountId) {
        if (accountId <= 0) {
            throw TransactionException.invalidId();
        }
        List<Transaction> list = repository.findByAccountId(accountId);
        if (list.isEmpty()) {
            throw TransactionException.emptyList();
        }
        return list;
    }

    @Override
    public List<Transaction> getByFromAccountId(long accountId) {
        if (accountId <= 0) {
            throw TransactionException.invalidId();
        }
        List<Transaction> list = repository.findByFromAccountId(accountId);
        if (list.isEmpty()) {
            throw TransactionException.emptyList();
        }
        return list;
    }

    @Override
    public List<Transaction> getByToAccountId(long accountId) {
        if (accountId <= 0) {
            throw TransactionException.invalidId();
        }
        List<Transaction> list = repository.findByToAccountId(accountId);
        if (list.isEmpty()) {
            throw TransactionException.emptyList();
        }
        return list;
    }

    @Override
    public List<Transaction> getByType(Transaction.TransactionType type) {
        if (type == null) {
            throw TransactionException.invalidType();
        }
        List<Transaction> list = repository.findByType(type);
        if (list.isEmpty()) {
            throw TransactionException.emptyList();
        }
        return list;
    }

    @Override
    public List<Transaction> getByStatus(Transaction.TransactionStatus status) {
        if (status == null) {
            throw TransactionException.invalidStatus();
        }
        List<Transaction> list = repository.findByStatus(status);
        if (list.isEmpty()) {
            throw TransactionException.emptyList();
        }
        return list;
    }

    private void validateForCreate(Transaction t) {
        if (t == null) {
            throw TransactionException.transactionIsNull();
        }
        if (t.getId() != null) {
            throw TransactionException.invalidId();
        }
        if (t.getAmount() == null || t.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw TransactionException.invalidAmount();
        }
        if (StringUtils.isNullOrEmpty(t.getCurrency())) {
            throw TransactionException.invalidCurrency();
        }
        if (t.getTransactionType() == null) {
            throw TransactionException.invalidType();
        }
        if (t.getStatus() == null) {
            throw TransactionException.invalidStatus();
        }
        if (t.getProcessedAt() == null) {
            t.setProcessedAt(LocalDateTime.now());
        }
    }

    private void validateForUpdate(Transaction t) {
        if (t == null) {
            throw TransactionException.transactionIsNull();
        }
        if (t.getId() == null || t.getId() <= 0) {
            throw TransactionException.invalidId();
        }
        if (t.getAmount() == null || t.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw TransactionException.invalidAmount();
        }
        if (StringUtils.isNullOrEmpty(t.getCurrency())) {
            throw TransactionException.invalidCurrency();
        }
        if (t.getTransactionType() == null) {
            throw TransactionException.invalidType();
        }
        if (t.getStatus() == null) {
            throw TransactionException.invalidStatus();
        }
        if (t.getProcessedAt() == null) {
            t.setProcessedAt(LocalDateTime.now());
        }
    }
}
