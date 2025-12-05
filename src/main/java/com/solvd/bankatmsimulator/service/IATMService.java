package com.solvd.bankatmsimulator.service;

import com.solvd.bankatmsimulator.domain.entity.ATM;
import java.util.List;

public interface IATMService {

    ATM register(ATM atm);

    ATM update(ATM atm);

    ATM getById(long id);

    List<ATM> getAll();

    void delete(long id);

    List<ATM> getActiveATMs();

    List<ATM> getByLocation(String location);
}
