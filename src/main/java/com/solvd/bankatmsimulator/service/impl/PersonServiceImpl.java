package com.solvd.bankatmsimulator.service.impl;

import com.mysql.cj.util.StringUtils;
import com.solvd.bankatmsimulator.domain.entity.Person;
import com.solvd.bankatmsimulator.domain.exception.PersonException;
import com.solvd.bankatmsimulator.persistence.IPersonRepository;
import com.solvd.bankatmsimulator.persistence.impl.PersonRepositoryImpl;
import com.solvd.bankatmsimulator.service.IPersonService;

import java.util.List;

public class PersonServiceImpl implements IPersonService {

    private final IPersonRepository repository;

    public PersonServiceImpl() {
        this.repository = new PersonRepositoryImpl();
    }

    @Override
    public Person register(Person person) {
        validateForCreate(person);
        return repository.create(person);
    }

    @Override
    public Person update(Person person) {
        validateForUpdate(person);
        return repository.update(person);
    }

    @Override
    public Person getById(long id) {
        if (id <= 0) {
            throw PersonException.invalidId();
        }
        return repository.findById(id)
                .orElseThrow(() -> PersonException.notFound(id));
    }

    @Override
    public List<Person> getAll() {
        List<Person> persons = repository.findAll();
        if (persons == null || persons.isEmpty()) {
            throw PersonException.emptyList();
        }
        return persons;
    }

    @Override
    public void delete(long id) {
        if (id <= 0) {
            throw PersonException.invalidId();
        }
        if (repository.findById(id).isEmpty()) {
            throw PersonException.notFound(id);
        }
        repository.delete(id);
    }

    @Override
    public Person getByEmail(String email) {
        if (StringUtils.isNullOrEmpty(email)) {
            throw PersonException.invalidEmail();
        }
        return repository.findByEmail(email)
                .orElseThrow(PersonException::invalidEmail);
    }

    @Override
    public Person getByPhoneNumber(String phoneNumber) {
        if (StringUtils.isNullOrEmpty(phoneNumber)) {
            throw PersonException.invalidPhone();
        }
        return repository.findByPhoneNumber(phoneNumber)
                .orElseThrow(PersonException::invalidPhone);
    }

    private void validateForCreate(Person person) {
        if (person == null) {
            throw PersonException.personIsNull();
        }
        if (person.getId() != null) {
            throw PersonException.invalidId();
        }
        if (StringUtils.isNullOrEmpty(person.getFullName())) {
            throw PersonException.invalidFullName();
        }
        if (StringUtils.isNullOrEmpty(person.getEmail())) {
            throw PersonException.invalidEmail();
        }
        if (StringUtils.isNullOrEmpty(person.getPhoneNumber())) {
            throw PersonException.invalidPhone();
        }
    }

    private void validateForUpdate(Person person) {
        if (person == null) {
            throw PersonException.personIsNull();
        }
        if (person.getId() == null || person.getId() <= 0) {
            throw PersonException.invalidId();
        }
        if (StringUtils.isNullOrEmpty(person.getFullName())) {
            throw PersonException.invalidFullName();
        }
        if (StringUtils.isNullOrEmpty(person.getEmail())) {
            throw PersonException.invalidEmail();
        }
        if (StringUtils.isNullOrEmpty(person.getPhoneNumber())) {
            throw PersonException.invalidPhone();
        }
    }
}
