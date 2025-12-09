package com.solvd.bankatmsimulator.service;

import com.solvd.bankatmsimulator.domain.Person;

import java.util.List;

public interface IPersonService {

    Person register(Person person);

    Person update(Person person);

    Person getById(long id);

    List<Person> getAll();

    void delete(long id);

    Person getByEmail(String email);

    Person getByPhoneNumber(String phoneNumber);
}
