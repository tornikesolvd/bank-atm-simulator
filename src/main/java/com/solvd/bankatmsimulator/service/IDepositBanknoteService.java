package com.solvd.bankatmsimulator.service;

import com.solvd.bankatmsimulator.domain.DepositBanknote;

import java.util.List;

public interface IDepositBanknoteService {

    DepositBanknote register(DepositBanknote banknote);

    DepositBanknote update(DepositBanknote banknote);

    DepositBanknote getById(long id);

    List<DepositBanknote> getAll();

    void delete(long id);

    List<DepositBanknote> getByDepositId(long depositId);
}
