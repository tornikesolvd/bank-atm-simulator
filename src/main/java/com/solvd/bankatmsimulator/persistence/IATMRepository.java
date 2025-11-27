package com.solvd.bankatmsimulator.persistence;

import com.solvd.bankatmsimulator.domain.ATM;

import java.util.List;

public interface IATMRepository extends IRepository<ATM> {
    
    List<ATM> findActiveATMs();
    
    List<ATM> findByLocation(String location);
}

