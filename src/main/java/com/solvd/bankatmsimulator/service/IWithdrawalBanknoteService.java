package com.solvd.bankatmsimulator.service;

import com.solvd.bankatmsimulator.domain.entity.WithdrawalBanknote;
import java.util.List;

public interface IWithdrawalBanknoteService {

    WithdrawalBanknote register(WithdrawalBanknote banknote);

    WithdrawalBanknote update(WithdrawalBanknote banknote);

    WithdrawalBanknote getById(long id);

    List<WithdrawalBanknote> getAll();

    void delete(long id);

    List<WithdrawalBanknote> getByWithdrawalId(long withdrawalId);
}
