package repository.Impl;

import base.repository.BaseRepositoryImpl;
import domain.Course;
import domain.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repository.EmployeeRepository;

import java.util.Optional;

public class EmployeeRepositoryImpl extends BaseRepositoryImpl<Employee, Integer> implements EmployeeRepository {


    public EmployeeRepositoryImpl(Session session) {
        super ( session );
    }

    @Override
    public Class<Employee> getEntityClass() {
        return Employee.class;
    }

    @Override
    public boolean isExistByUsername(String username) {
         return ((session.createQuery ( "SELECT count(e) FROM " + entityClass.getSimpleName () + " e", Long.class )
                .getSingleResult ())>0);
    }

}
