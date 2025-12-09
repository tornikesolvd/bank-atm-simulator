package com.solvd.bankatmsimulator.service;

import com.solvd.bankatmsimulator.domain.ATMBanknote;

import java.util.List;

public interface IATMBanknoteService {

    ATMBanknote register(ATMBanknote banknote);

    ATMBanknote update(ATMBanknote banknote);

    ATMBanknote getById(long id);

    List<ATMBanknote> getAll();

    void delete(long id);

    List<ATMBanknote> getByAtmId(long atmId);

    List<ATMBanknote> getByAtmIdAndCurrency(long atmId, String currency);
}
