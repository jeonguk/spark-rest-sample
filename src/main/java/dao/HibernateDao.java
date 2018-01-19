package dao;

import java.io.Serializable;

public interface HibernateDao<T, PK extends Serializable> {

    T create(T t);

    T update(T t);

    T findById(PK id);

    void delete(T t);

}