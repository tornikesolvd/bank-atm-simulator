package com.solvd.bankatmsimulator.persistence;

import com.solvd.bankatmsimulator.domain.entity.Person;

import java.util.Optional;

public interface IPersonRepository extends IRepository<Person> {
    
    Optional<Person> findByEmail(String email);
    
    Optional<Person> findByPhoneNumber(String phoneNumber);
}

