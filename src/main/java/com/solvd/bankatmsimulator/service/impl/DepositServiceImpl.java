package com.solvd.bankatmsimulator.service.impl;

import com.mysql.cj.util.StringUtils;
import com.solvd.bankatmsimulator.domain.Deposit;
import com.solvd.bankatmsimulator.exception.DepositException;
import com.solvd.bankatmsimulator.persistence.IDepositRepository;
import com.solvd.bankatmsimulator.persistence.impl.DepositRepositoryImpl;
import com.solvd.bankatmsimulator.service.IDepositService;

import java.math.BigDecimal;
import java.util.List;

public class DepositServiceImpl implements IDepositService {

    private final IDepositRepository repository;

    public DepositServiceImpl() {
        this.repository = new DepositRepositoryImpl();
    }

    @Override
    public Deposit register(Deposit deposit) {
        validateForCreate(deposit);
        return repository.create(deposit);
    }

    @Override
    public Deposit update(Deposit deposit) {
        validateForUpdate(deposit);
        return repository.update(deposit);
    }

    @Override
    public Deposit getById(long id) {
        if (id <= 0) {
            throw DepositException.invalidId();
        }
        return repository.findById(id)
                .orElseThrow(() -> DepositException.notFound(id));
    }

    @Override
    public List<Deposit> getAll() {
        List<Deposit> deposits = repository.findAll();
        if (deposits == null || deposits.isEmpty()) {
            throw DepositException.emptyList();
        }
        return deposits;
    }

    @Override
    public void delete(long id) {
        if (id <= 0) {
            throw DepositException.invalidId();
        }
        if (repository.findById(id).isEmpty()) {
            throw DepositException.notFound(id);
        }
        repository.delete(id);
    }

    @Override
    public List<Deposit> getByTransactionId(long transactionId) {
        if (transactionId <= 0) {
            throw DepositException.invalidTransactionId();
        }
        List<Deposit> deposits = repository.findByTransactionId(transactionId);
        if (deposits.isEmpty()) {
            throw DepositException.emptyList();
        }
        return deposits;
    }

    @Override
    public List<Deposit> getByAtmId(long atmId) {
        if (atmId <= 0) {
            throw DepositException.invalidAtmId();
        }
        List<Deposit> deposits = repository.findByAtmId(atmId);
        if (deposits.isEmpty()) {
            throw DepositException.emptyList();
        }
        return deposits;
    }

    private void validateForCreate(Deposit deposit) {
        if (deposit == null) {
            throw DepositException.depositIsNull();
        }
        if (deposit.getId() != null) {
            throw DepositException.invalidId();
        }
        if (deposit.getTransactionId() == null || deposit.getTransactionId() <= 0) {
            throw DepositException.invalidTransactionId();
        }
        if (deposit.getAtmId() == null || deposit.getAtmId() <= 0) {
            throw DepositException.invalidAtmId();
        }
        if (StringUtils.isNullOrEmpty(deposit.getCurrency())) {
            throw DepositException.invalidCurrency();
        }
        if (deposit.getTotalAmount() == null || deposit.getTotalAmount().compareTo(BigDecimal.ONE) < 0) {
            throw DepositException.invalidAmount();
        }
        if (deposit.getProcessedAt() == null) {
            throw DepositException.invalidProcessedAt();
        }
    }

    private void validateForUpdate(Deposit deposit) {
        if (deposit == null) {
            throw DepositException.depositIsNull();
        }
        if (deposit.getId() == null || deposit.getId() <= 0) {
            throw DepositException.invalidId();
        }
        if (deposit.getTransactionId() == null || deposit.getTransactionId() <= 0) {
            throw DepositException.invalidTransactionId();
        }
        if (deposit.getAtmId() == null || deposit.getAtmId() <= 0) {
            throw DepositException.invalidAtmId();
        }
        if (StringUtils.isNullOrEmpty(deposit.getCurrency())) {
            throw DepositException.invalidCurrency();
        }
        if (deposit.getTotalAmount() == null || deposit.getTotalAmount().compareTo(BigDecimal.ONE) < 0) {
            throw DepositException.invalidAmount();
        }
        if (deposit.getProcessedAt() == null) {
            throw DepositException.invalidProcessedAt();
        }
    }
}
