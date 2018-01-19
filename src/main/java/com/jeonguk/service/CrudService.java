package com.jeonguk.service;

import java.io.Serializable;

public interface CrudService<T, PK extends Serializable> {

    T create(T t);

    T update(T t);

    T findById(PK id);

    void delete(T t);

}