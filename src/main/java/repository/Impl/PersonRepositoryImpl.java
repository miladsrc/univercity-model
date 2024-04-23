package repository.Impl;

import base.repository.BaseRepositoryImpl;
import domain.Person;
import org.hibernate.Session;
import repository.PersonRepository;

public class PersonRepositoryImpl extends BaseRepositoryImpl<Person, Integer> implements PersonRepository {
    public PersonRepositoryImpl(Session session) {
        super ( session );
    }
    @Override
    public Class<Person> getEntityClass() {
        return Person.class;
    }
}
