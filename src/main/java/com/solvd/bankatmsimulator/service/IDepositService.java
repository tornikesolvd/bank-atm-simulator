package com.solvd.bankatmsimulator.service;

import com.solvd.bankatmsimulator.domain.Deposit;

import java.util.List;

public interface IDepositService {

    Deposit register(Deposit deposit);

    Deposit update(Deposit deposit);

    Deposit getById(long id);

    List<Deposit> getAll();

    void delete(long id);

    List<Deposit> getByTransactionId(long transactionId);

    List<Deposit> getByAtmId(long atmId);
}
