package com.solvd.bankatmsimulator.persistence;

import com.solvd.bankatmsimulator.domain.Withdrawal;

import java.util.List;

public interface IWithdrawalRepository extends IRepository<Withdrawal> {

    List<Withdrawal> findByAccountId(Long accountId);

    List<Withdrawal> findByTransactionId(Long transactionId);

    List<Withdrawal> findByAtmId(Long atmId);
}

