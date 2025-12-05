package com.solvd.bankatmsimulator.service.impl;

import com.mysql.cj.util.StringUtils;
import com.solvd.bankatmsimulator.domain.entity.Account;
import com.solvd.bankatmsimulator.domain.exception.AccountException;
import com.solvd.bankatmsimulator.persistence.IAccountRepository;
import com.solvd.bankatmsimulator.persistence.impl.AccountRepositoryImpl;
import com.solvd.bankatmsimulator.service.IAccountService;

import java.util.List;

public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository repository;

    public AccountServiceImpl() {
        this.repository = new AccountRepositoryImpl();
    }

    @Override
    public Account register(Account account) {
        validateForCreate(account);
        return repository.create(account);
    }

    @Override
    public Account update(Account account) {
        validateForUpdate(account);
        return repository.update(account);
    }

    @Override
    public Account getById(long id) {
        if (id <= 0) {
            throw AccountException.invalidId();
        }
        return repository.findById(id)
                .orElseThrow(() -> AccountException.notFound(id));
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = repository.findAll();
        if (accounts == null || accounts.isEmpty()) {
            throw AccountException.emptyList();
        }
        return accounts;
    }

    @Override
    public void delete(long id) {
        if (id <= 0) {
            throw AccountException.invalidId();
        }
        if (repository.findById(id).isEmpty()) {
            throw AccountException.notFound(id);
        }
        repository.delete(id);
    }

    @Override
    public Account getByAccountNumber(String accountNumber) {
        if (StringUtils.isNullOrEmpty(accountNumber)) {
            throw AccountException.invalidNumber();
        }
        return repository.findByAccountNumber(accountNumber)
                .orElseThrow(AccountException::accountIsNull);
    }

    @Override
    public List<Account> getByCurrency(String currency) {
        if (StringUtils.isNullOrEmpty(currency)) {
            throw AccountException.invalidCurrency();
        }
        List<Account> accounts = repository.findByCurrency(currency);
        if (accounts.isEmpty()) {
            throw AccountException.emptyList();
        }
        return accounts;
    }

    @Override
    public List<Account> getByPersonId(long personId) {
        if (personId <= 0) {
            throw AccountException.invalidId();
        }
        List<Account> accounts = repository.findByPersonId(personId);
        if (accounts.isEmpty()) {
            throw AccountException.emptyList();
        }
        return accounts;
    }

    private void validateForCreate(Account account) {
        if (account == null) {
            throw AccountException.accountIsNull();
        }
        if (StringUtils.isNullOrEmpty(account.getAccountNumber())) {
            throw AccountException.invalidNumber();
        }
        if (account.getId() != null) {
            throw AccountException.invalidId();
        }
    }

    private void validateForUpdate(Account account) {
        if (account == null) {
            throw AccountException.accountIsNull();
        }
        if (account.getId() == null || account.getId() <= 0) {
            throw AccountException.invalidId();
        }
        if (StringUtils.isNullOrEmpty(account.getAccountNumber())) {
            throw AccountException.invalidNumber();
        }
    }
}
