package com.solvd.bankatmsimulator.persistence;

import com.solvd.bankatmsimulator.domain.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountRepository extends IRepository<Account> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    List<Account> findByCurrency(String currency);
    
    List<Account> findByPersonId(Long personId);
}

