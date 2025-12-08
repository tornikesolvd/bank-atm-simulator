package com.solvd.bankatmsimulator.service;

import com.solvd.bankatmsimulator.domain.Account;
import java.util.List;

public interface IAccountService {

    Account register(Account account);

    Account update(Account account);

    Account getById(long id);

    List<Account> getAll();

    void delete(long id);

    Account getByAccountNumber(String accountNumber);

    List<Account> getByCurrency(String currency);

    List<Account> getByPersonId(long personId);
}
