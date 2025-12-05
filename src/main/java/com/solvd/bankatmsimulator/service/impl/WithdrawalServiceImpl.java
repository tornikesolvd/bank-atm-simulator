package com.solvd.bankatmsimulator.service.impl;

import com.mysql.cj.util.StringUtils;
import com.solvd.bankatmsimulator.domain.entity.Withdrawal;
import com.solvd.bankatmsimulator.domain.exception.WithdrawalException;
import com.solvd.bankatmsimulator.persistence.IWithdrawalRepository;
import com.solvd.bankatmsimulator.persistence.impl.WithdrawalRepositoryImpl;
import com.solvd.bankatmsimulator.service.IWithdrawalService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class WithdrawalServiceImpl implements IWithdrawalService {

    private final IWithdrawalRepository repository;

    public WithdrawalServiceImpl() {
        this.repository = new WithdrawalRepositoryImpl();
    }

    @Override
    public Withdrawal register(Withdrawal withdrawal) {
        validateForCreate(withdrawal);
        return repository.create(withdrawal);
    }

    @Override
    public Withdrawal update(Withdrawal withdrawal) {
        validateForUpdate(withdrawal);
        return repository.update(withdrawal);
    }

    @Override
    public Withdrawal getById(long id) {
        if (id <= 0) {
            throw WithdrawalException.invalidId();
        }
        return repository.findById(id)
                .orElseThrow(() -> WithdrawalException.notFound(id));
    }

    @Override
    public List<Withdrawal> getAll() {
        List<Withdrawal> withdrawals = repository.findAll();
        if (withdrawals == null || withdrawals.isEmpty()) {
            throw WithdrawalException.emptyList();
        }
        return withdrawals;
    }

    @Override
    public void delete(long id) {
        if (id <= 0) {
            throw WithdrawalException.invalidId();
        }
        if (repository.findById(id).isEmpty()) {
            throw WithdrawalException.notFound(id);
        }
        repository.delete(id);
    }

    @Override
    public List<Withdrawal> getByAccountId(long accountId) {
        if (accountId <= 0) {
            throw WithdrawalException.invalidAccountId();
        }
        List<Withdrawal> list = repository.findByAccountId(accountId);
        if (list.isEmpty()) {
            throw WithdrawalException.emptyList();
        }
        return list;
    }

    @Override
    public List<Withdrawal> getByTransactionId(long transactionId) {
        if (transactionId <= 0) {
            throw WithdrawalException.invalidTransactionId();
        }
        List<Withdrawal> list = repository.findByTransactionId(transactionId);
        if (list.isEmpty()) {
            throw WithdrawalException.emptyList();
        }
        return list;
    }

    @Override
    public List<Withdrawal> getByAtmId(long atmId) {
        if (atmId <= 0) {
            throw WithdrawalException.invalidAtmId();
        }
        List<Withdrawal> list = repository.findByAtmId(atmId);
        if (list.isEmpty()) {
            throw WithdrawalException.emptyList();
        }
        return list;
    }

    private void validateForCreate(Withdrawal w) {
        if (w == null) {
            throw WithdrawalException.entityIsNull();
        }
        if (w.getId() != null) {
            throw WithdrawalException.invalidId();
        }
        validateCommonFields(w);
        if (w.getProcessedAt() == null) {
            w.setProcessedAt(LocalDateTime.now());
        }
    }

    private void validateForUpdate(Withdrawal w) {
        if (w == null) {
            throw WithdrawalException.entityIsNull();
        }
        if (w.getId() == null || w.getId() <= 0) {
            throw WithdrawalException.invalidId();
        }
        validateCommonFields(w);
        if (w.getProcessedAt() == null) {
            w.setProcessedAt(LocalDateTime.now());
        }
    }

    private void validateCommonFields(Withdrawal w) {
        if (w.getAccountId() == null || w.getAccountId() <= 0) {
            throw WithdrawalException.invalidAccountId();
        }
        if (w.getTransactionId() == null || w.getTransactionId() <= 0) {
            throw WithdrawalException.invalidTransactionId();
        }
        if (w.getAtmId() == null || w.getAtmId() <= 0) {
            throw WithdrawalException.invalidAtmId();
        }
        if (w.getTotalAmount() == null || w.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw WithdrawalException.invalidAmount();
        }
        if (w.getTotalAmount().compareTo(Withdrawal.MAX_AMOUNT) > 0) {
            throw WithdrawalException.invalidAmount();
        }
        if (StringUtils.isNullOrEmpty(w.getCurrency())) {
            throw WithdrawalException.invalidCurrency();
        }
    }
}
