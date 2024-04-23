package service.Impl;

import base.service.BaseServiceImpl;
import domain.Employee;
import org.hibernate.SessionFactory;
import repository.EmployeeRepository;
import service.EmployeeService;

public class EmployeeServiceImpl extends BaseServiceImpl<Employee, Integer, EmployeeRepository> implements EmployeeService {

    public EmployeeServiceImpl(EmployeeRepository repository ) {
        super ( repository );
    }
}