package service.Impl;

import base.repository.BaseRepositoryImpl;
import base.service.BaseServiceImpl;
import domain.Person;
import repository.PersonRepository;
import service.PersonService;

public class PersonServiceImpl extends BaseServiceImpl<Person, Integer, PersonRepository>
implements PersonService {
    public PersonServiceImpl(PersonRepository repository) {
        super ( repository );
    }
}
