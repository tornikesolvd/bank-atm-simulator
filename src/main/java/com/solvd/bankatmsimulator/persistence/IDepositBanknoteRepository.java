package com.solvd.bankatmsimulator.persistence;

import com.solvd.bankatmsimulator.domain.entity.DepositBanknote;

import java.util.List;

public interface IDepositBanknoteRepository extends IRepository<DepositBanknote> {
    
    List<DepositBanknote> findByDepositId(Long depositId);
}

