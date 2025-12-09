package com.solvd.bankatmsimulator.persistence;

import com.solvd.bankatmsimulator.domain.WithdrawalBanknote;

import java.util.List;

public interface IWithdrawalBanknoteRepository extends IRepository<WithdrawalBanknote> {

    List<WithdrawalBanknote> findByWithdrawalId(Long withdrawalId);
}

