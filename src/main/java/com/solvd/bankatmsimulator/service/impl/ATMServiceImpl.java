package com.solvd.bankatmsimulator.service.impl;

import com.mysql.cj.util.StringUtils;
import com.solvd.bankatmsimulator.domain.entity.ATM;
import com.solvd.bankatmsimulator.domain.exception.ATMException;
import com.solvd.bankatmsimulator.persistence.IATMRepository;
import com.solvd.bankatmsimulator.persistence.impl.ATMRepositoryImpl;
import com.solvd.bankatmsimulator.service.IATMService;

import java.util.List;

public class ATMServiceImpl implements IATMService {

    private final IATMRepository repository;

    public ATMServiceImpl() {
        this.repository = new ATMRepositoryImpl();
    }

    @Override
    public ATM register(ATM atm) {
        validateForCreate(atm);
        return repository.create(atm);
    }

    @Override
    public ATM update(ATM atm) {
        validateForUpdate(atm);
        return repository.update(atm);
    }

    @Override
    public ATM getById(long id) {
        if (id <= 0) {
            throw ATMException.invalidId();
        }
        return repository.findById(id)
                .orElseThrow(() -> ATMException.notFound(id));
    }

    @Override
    public List<ATM> getAll() {
        List<ATM> atms = repository.findAll();
        if (atms == null || atms.isEmpty()) {
            throw ATMException.emptyList();
        }
        return atms;
    }

    @Override
    public void delete(long id) {
        if (id <= 0) {
            throw ATMException.invalidId();
        }
        if (repository.findById(id).isEmpty()) {
            throw ATMException.notFound(id);
        }
        repository.delete(id);
    }

    @Override
    public List<ATM> getActiveATMs() {
        List<ATM> atms = repository.findActiveATMs();
        if (atms.isEmpty()) {
            throw ATMException.emptyList();
        }
        return atms;
    }

    @Override
    public List<ATM> getByLocation(String location) {
        if (StringUtils.isNullOrEmpty(location)) {
            throw ATMException.invalidLocation();
        }
        List<ATM> atms = repository.findByLocation(location);
        if (atms.isEmpty()) {
            throw ATMException.emptyList();
        }
        return atms;
    }

    private void validateForCreate(ATM atm) {
        if (atm == null) {
            throw ATMException.atmIsNull();
        }
        if (StringUtils.isNullOrEmpty(atm.getLocation())) {
            throw ATMException.invalidLocation();
        }
        if (StringUtils.isNullOrEmpty(atm.getName())) {
            throw ATMException.invalidName();
        }
        if (atm.getId() != null) {
            throw ATMException.invalidId();
        }
    }

    private void validateForUpdate(ATM atm) {
        if (atm == null) {
            throw ATMException.atmIsNull();
        }
        if (atm.getId() == null || atm.getId() <= 0) {
            throw ATMException.invalidId();
        }
        if (StringUtils.isNullOrEmpty(atm.getLocation())) {
            throw ATMException.invalidLocation();
        }
        if (StringUtils.isNullOrEmpty(atm.getName())) {
            throw ATMException.invalidName();
        }
    }
}
