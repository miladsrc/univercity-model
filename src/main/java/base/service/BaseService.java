package base.service;

import base.entity.BaseEntity;
import jakarta.persistence.EntityTransaction;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseEntity<ID>, ID extends Serializable> {


    T saveOrUpdate(T entity);

    T findById(ID id);

    void delete(T t);

    //added
    List<T> findAll();

    void deleteById(ID id);

    Optional<T> existsByUsernameAndPassword(String username, String password);

    Long countAll();

    //plus one --> we use them in service layer if we have to...
    void beginTransaction();

    void commitTransaction();

    void rollbackTransaction();

    EntityTransaction getTransaction();

}
