package com.solvd.bankatmsimulator.service.impl;

import com.mysql.cj.util.StringUtils;
import com.solvd.bankatmsimulator.domain.entity.ATMBanknote;
import com.solvd.bankatmsimulator.domain.exception.ATMBanknoteException;
import com.solvd.bankatmsimulator.persistence.IATMBanknoteRepository;
import com.solvd.bankatmsimulator.persistence.impl.ATMBanknoteRepositoryImpl;
import com.solvd.bankatmsimulator.service.IATMBanknoteService;

import java.math.BigDecimal;
import java.util.List;

public class ATMBanknoteServiceImpl implements IATMBanknoteService {

    private final IATMBanknoteRepository repository;

    public ATMBanknoteServiceImpl() {
        this.repository = new ATMBanknoteRepositoryImpl();
    }

    @Override
    public ATMBanknote register(ATMBanknote banknote) {
        validateForCreate(banknote);
        return repository.create(banknote);
    }

    @Override
    public ATMBanknote update(ATMBanknote banknote) {
        validateForUpdate(banknote);
        return repository.update(banknote);
    }

    @Override
    public ATMBanknote getById(long id) {
        if (id <= 0) {
            throw ATMBanknoteException.invalidId();
        }
        return repository.findById(id)
                .orElseThrow(() -> ATMBanknoteException.notFound(id));
    }

    @Override
    public List<ATMBanknote> getAll() {
        List<ATMBanknote> banknotes = repository.findAll();
        if (banknotes == null || banknotes.isEmpty()) {
            throw ATMBanknoteException.emptyList();
        }
        return banknotes;
    }

    @Override
    public void delete(long id) {
        if (id <= 0) {
            throw ATMBanknoteException.invalidId();
        }
        if (repository.findById(id).isEmpty()) {
            throw ATMBanknoteException.notFound(id);
        }
        repository.delete(id);
    }

    @Override
    public List<ATMBanknote> getByAtmId(long atmId) {
        if (atmId <= 0) {
            throw ATMBanknoteException.invalidAtmId();
        }
        List<ATMBanknote> banknotes = repository.findByAtmId(atmId);
        if (banknotes.isEmpty()) {
            throw ATMBanknoteException.emptyList();
        }
        return banknotes;
    }

    @Override
    public List<ATMBanknote> getByAtmIdAndCurrency(long atmId, String currency) {
        if (atmId <= 0) {
            throw ATMBanknoteException.invalidAtmId();
        }
        if (StringUtils.isNullOrEmpty(currency)) {
            throw ATMBanknoteException.invalidCurrency();
        }
        List<ATMBanknote> banknotes = repository.findByAtmIdAndCurrency(atmId, currency);
        if (banknotes.isEmpty()) {
            throw ATMBanknoteException.emptyList();
        }
        return banknotes;
    }

    private void validateForCreate(ATMBanknote banknote) {
        if (banknote == null) {
            throw ATMBanknoteException.banknoteIsNull();
        }
        if (banknote.getId() != null) {
            throw ATMBanknoteException.invalidId();
        }
        if (banknote.getAtmId() == null || banknote.getAtmId() <= 0) {
            throw ATMBanknoteException.invalidAtmId();
        }
        if (StringUtils.isNullOrEmpty(banknote.getCurrency())) {
            throw ATMBanknoteException.invalidCurrency();
        }
        if (banknote.getDenomination() == null || banknote.getDenomination().compareTo(BigDecimal.ZERO) <= 0) {
            throw ATMBanknoteException.invalidDenomination();
        }
        if (banknote.getQuantity() == null || banknote.getQuantity() < 0) {
            throw ATMBanknoteException.invalidQuantity();
        }
    }

    private void validateForUpdate(ATMBanknote banknote) {
        if (banknote == null) {
            throw ATMBanknoteException.banknoteIsNull();
        }
        if (banknote.getId() == null || banknote.getId() <= 0) {
            throw ATMBanknoteException.invalidId();
        }
        if (banknote.getAtmId() == null || banknote.getAtmId() <= 0) {
            throw ATMBanknoteException.invalidAtmId();
        }
        if (StringUtils.isNullOrEmpty(banknote.getCurrency())) {
            throw ATMBanknoteException.invalidCurrency();
        }
        if (banknote.getDenomination() == null || banknote.getDenomination().compareTo(BigDecimal.ZERO) <= 0) {
            throw ATMBanknoteException.invalidDenomination();
        }
        if (banknote.getQuantity() == null || banknote.getQuantity() < 0) {
            throw ATMBanknoteException.invalidQuantity();
        }
    }
}
