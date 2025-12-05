package com.solvd.bankatmsimulator.persistence;

import com.solvd.bankatmsimulator.domain.entity.Deposit;

import java.util.List;

public interface IDepositRepository extends IRepository<Deposit> {
    
    List<Deposit> findByTransactionId(Long transactionId);
    
    List<Deposit> findByAtmId(Long atmId);
}

