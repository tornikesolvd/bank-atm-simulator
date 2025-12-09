package com.solvd.bankatmsimulator.service;

import com.solvd.bankatmsimulator.domain.Withdrawal;

import java.util.List;

public interface IWithdrawalService {

    Withdrawal register(Withdrawal withdrawal);

    Withdrawal update(Withdrawal withdrawal);

    Withdrawal getById(long id);

    List<Withdrawal> getAll();

    void delete(long id);

    List<Withdrawal> getByAccountId(long accountId);

    List<Withdrawal> getByTransactionId(long transactionId);

    List<Withdrawal> getByAtmId(long atmId);
}
