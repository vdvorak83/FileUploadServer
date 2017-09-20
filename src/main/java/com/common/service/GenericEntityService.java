package com.common.service;

import java.util.List;

public interface GenericEntityService<E,PK>{

    E save(E newInstance) ;

    void delete(PK primaryKey);

    public void deleteAll();

    void update(E persistentInstance);

    E find(PK primaryKey);

    List<E> findAll();

    boolean exists(PK primaryKey);
}