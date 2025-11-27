package com.solvd.bankatmsimulator.persistence;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    
    T create(T entity);
    
    Optional<T> findById(Long id);
    
    List<T> findAll();
    
    T update(T entity);
    
    void delete(Long id);
}

