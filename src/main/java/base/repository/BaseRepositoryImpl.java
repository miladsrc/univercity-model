package base.repository;

import jakarta.persistence.EntityTransaction;
import base.entity.BaseEntity;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepositoryImpl<T extends BaseEntity<ID>, ID extends Serializable>
        implements BaseRepository<T, ID> {

    protected final Session session;



    protected final Class<T> entityClass;
    public abstract Class<T> getEntityClass();


    //CONSTRUCTOR
    public BaseRepositoryImpl(Session session) {
        this.session = session;
        this.entityClass = getEntityClass ();
    }


    //METHODS
    @Override
    public T saveOrUpdate(T entity) {
        if (entity.getId () == null) {
            session.persist ( entity );
        } else {
            entity = session.merge ( entity );
        }
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable (
                session.get ( entityClass, id )
        );
    }

    @Override
    public void delete(T entity) {
        if (entity != null) {
            session.remove ( entity );
        }
    }

    @Override
    public List<T> findAll() {
        return session.createQuery
                        ( "select e from " + entityClass.getSimpleName () + " e", entityClass )
                .getResultList ();
    }


    @Override
    public void deleteById(ID id) {

        //1- first method
//       Optional<T> t = findById ( id );
//       if ( t.isPresent () ) {}
//       {
//           session.remove ( t.get () );
//       }

//        2- second method with query parameters
//       session.createQuery
//                       ( "delete from "+ entityClass.getSimpleName () + " e"+ " where e.id = " + id)
//               .executeUpdate ();

        //3- second method with query parameters and parameters are named parameters which we name later
        session.createQuery
                        ( "DELETE  FROM " + entityClass.getSimpleName () + " e" + " where e.id = :myId " )
                .setParameter ( "myId", id )
                .executeUpdate ();
    }

    @Override
    public Long countAll() {
        return session.createQuery ( "SELECT count(e) FROM " + entityClass.getSimpleName () + " e", Long.class )
                .getSingleResult ();
        //---> چون میدانیم خروجی یک ردیف است پس سینگل ریزالت میگیریم
    }

    @Override
    public Optional<T> existsByUsernameAndPassword(String username, String password) {
            T user = session.createQuery(
                            "SELECT e FROM " + entityClass.getSimpleName() +
                                    " e where e.username= :username and e.password = :password", entityClass)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            return Optional.ofNullable(user);
    }


    @Override
    public void beginTransaction() {
        if (!session.getTransaction ().isActive ()) {
            session.getTransaction ().begin ();
        }
    }

    @Override
    public void commitTransaction() {
        session.getTransaction ().commit ();
    }

    @Override
    public void rollbackTransaction() {
        session.getTransaction ().rollback ();
    }

    @Override
    public EntityTransaction getTransaction() {
        return null;
    }
}

