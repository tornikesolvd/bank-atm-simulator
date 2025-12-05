package com.solvd.bankatmsimulator.persistence;

import com.solvd.bankatmsimulator.domain.entity.ATMBanknote;

import java.util.List;

public interface IATMBanknoteRepository extends IRepository<ATMBanknote> {
    
    List<ATMBanknote> findByAtmId(Long atmId);
    
    List<ATMBanknote> findByAtmIdAndCurrency(Long atmId, String currency);
}

