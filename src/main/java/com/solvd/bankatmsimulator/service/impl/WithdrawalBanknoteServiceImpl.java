package com.solvd.bankatmsimulator.service.impl;

import com.solvd.bankatmsimulator.domain.WithdrawalBanknote;
import com.solvd.bankatmsimulator.exception.WithdrawalBanknoteException;
import com.solvd.bankatmsimulator.persistence.IWithdrawalBanknoteRepository;
import com.solvd.bankatmsimulator.persistence.impl.WithdrawalBanknoteRepositoryImpl;
import com.solvd.bankatmsimulator.service.IWithdrawalBanknoteService;

import java.math.BigDecimal;
import java.util.List;

public class WithdrawalBanknoteServiceImpl implements IWithdrawalBanknoteService {

    private final IWithdrawalBanknoteRepository repository;

    public WithdrawalBanknoteServiceImpl() {
        this.repository = new WithdrawalBanknoteRepositoryImpl();
    }

    @Override
    public WithdrawalBanknote register(WithdrawalBanknote banknote) {
        validateForCreate(banknote);
        return repository.create(banknote);
    }

    @Override
    public WithdrawalBanknote update(WithdrawalBanknote banknote) {
        validateForUpdate(banknote);
        return repository.update(banknote);
    }

    @Override
    public WithdrawalBanknote getById(long id) {
        if (id <= 0) {
            throw WithdrawalBanknoteException.invalidId();
        }
        return repository.findById(id)
                .orElseThrow(() -> WithdrawalBanknoteException.notFound(id));
    }

    @Override
    public List<WithdrawalBanknote> getAll() {
        List<WithdrawalBanknote> list = repository.findAll();
        if (list == null || list.isEmpty()) {
            throw WithdrawalBanknoteException.emptyList();
        }
        return list;
    }

    @Override
    public void delete(long id) {
        if (id <= 0) {
            throw WithdrawalBanknoteException.invalidId();
        }
        if (repository.findById(id).isEmpty()) {
            throw WithdrawalBanknoteException.notFound(id);
        }
        repository.delete(id);
    }

    @Override
    public List<WithdrawalBanknote> getByWithdrawalId(long withdrawalId) {
        if (withdrawalId <= 0) {
            throw WithdrawalBanknoteException.invalidWithdrawalId();
        }
        List<WithdrawalBanknote> list = repository.findByWithdrawalId(withdrawalId);
        if (list.isEmpty()) {
            throw WithdrawalBanknoteException.emptyList();
        }
        return list;
    }

    private void validateForCreate(WithdrawalBanknote banknote) {
        if (banknote == null) {
            throw WithdrawalBanknoteException.entityIsNull();
        }
        if (banknote.getId() != null) {
            throw WithdrawalBanknoteException.invalidId();
        }
        validateCommonFields(banknote);
    }

    private void validateForUpdate(WithdrawalBanknote banknote) {
        if (banknote == null) {
            throw WithdrawalBanknoteException.entityIsNull();
        }
        if (banknote.getId() == null || banknote.getId() <= 0) {
            throw WithdrawalBanknoteException.invalidId();
        }
        validateCommonFields(banknote);
    }

    private void validateCommonFields(WithdrawalBanknote banknote) {
        if (banknote.getWithdrawalId() == null || banknote.getWithdrawalId() <= 0) {
            throw WithdrawalBanknoteException.invalidWithdrawalId();
        }
        if (banknote.getDenomination() == null ||
                banknote.getDenomination().compareTo(BigDecimal.ZERO) <= 0) {
            throw WithdrawalBanknoteException.invalidDenomination();
        }
        if (banknote.getQuantity() == null || banknote.getQuantity() <= 0) {
            throw WithdrawalBanknoteException.invalidQuantity();
        }
    }
}
