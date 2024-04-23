package service;

import base.service.BaseService;
import domain.Person;
import jakarta.persistence.criteria.CriteriaBuilder;

public interface PersonService extends BaseService<Person, Integer> {
}
