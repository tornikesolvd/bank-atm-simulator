package com.solvd.bankatmsimulator.service.impl;

import com.solvd.bankatmsimulator.domain.DepositBanknote;
import com.solvd.bankatmsimulator.exception.DepositBanknoteException;
import com.solvd.bankatmsimulator.persistence.IDepositBanknoteRepository;
import com.solvd.bankatmsimulator.persistence.impl.DepositBanknoteRepositoryImpl;
import com.solvd.bankatmsimulator.service.IDepositBanknoteService;

import java.math.BigDecimal;
import java.util.List;

public class DepositBanknoteServiceImpl implements IDepositBanknoteService {

    private final IDepositBanknoteRepository repository;

    public DepositBanknoteServiceImpl() {
        this.repository = new DepositBanknoteRepositoryImpl();
    }

    @Override
    public DepositBanknote register(DepositBanknote banknote) {
        validateForCreate(banknote);
        return repository.create(banknote);
    }

    @Override
    public DepositBanknote update(DepositBanknote banknote) {
        validateForUpdate(banknote);
        return repository.update(banknote);
    }

    @Override
    public DepositBanknote getById(long id) {
        if (id <= 0) {
            throw DepositBanknoteException.invalidId();
        }
        return repository.findById(id)
                .orElseThrow(() -> DepositBanknoteException.notFound(id));
    }

    @Override
    public List<DepositBanknote> getAll() {
        List<DepositBanknote> banknotes = repository.findAll();
        if (banknotes == null || banknotes.isEmpty()) {
            throw DepositBanknoteException.emptyList();
        }
        return banknotes;
    }

    @Override
    public void delete(long id) {
        if (id <= 0) {
            throw DepositBanknoteException.invalidId();
        }
        if (repository.findById(id).isEmpty()) {
            throw DepositBanknoteException.notFound(id);
        }
        repository.delete(id);
    }

    @Override
    public List<DepositBanknote> getByDepositId(long depositId) {
        if (depositId <= 0) {
            throw DepositBanknoteException.invalidDepositId();
        }
        List<DepositBanknote> banknotes = repository.findByDepositId(depositId);
        if (banknotes.isEmpty()) {
            throw DepositBanknoteException.emptyList();
        }
        return banknotes;
    }

    private void validateForCreate(DepositBanknote banknote) {
        if (banknote == null) {
            throw DepositBanknoteException.banknoteIsNull();
        }
        if (banknote.getId() != null) {
            throw DepositBanknoteException.invalidId();
        }
        if (banknote.getDepositId() == null || banknote.getDepositId() <= 0) {
            throw DepositBanknoteException.invalidDepositId();
        }
        if (banknote.getDenomination() == null || banknote.getDenomination().compareTo(BigDecimal.ZERO) <= 0) {
            throw DepositBanknoteException.invalidDenomination();
        }
        if (banknote.getQuantity() == null || banknote.getQuantity() < 0) {
            throw DepositBanknoteException.invalidQuantity();
        }
    }

    private void validateForUpdate(DepositBanknote banknote) {
        if (banknote == null) {
            throw DepositBanknoteException.banknoteIsNull();
        }
        if (banknote.getId() == null || banknote.getId() <= 0) {
            throw DepositBanknoteException.invalidId();
        }
        if (banknote.getDepositId() == null || banknote.getDepositId() <= 0) {
            throw DepositBanknoteException.invalidDepositId();
        }
        if (banknote.getDenomination() == null || banknote.getDenomination().compareTo(BigDecimal.ZERO) <= 0) {
            throw DepositBanknoteException.invalidDenomination();
        }
        if (banknote.getQuantity() == null || banknote.getQuantity() < 0) {
            throw DepositBanknoteException.invalidQuantity();
        }
    }
}
