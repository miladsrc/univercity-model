package repository;

import base.repository.BaseRepository;
import domain.Course;
import domain.Employee;

public interface EmployeeRepository extends BaseRepository<Employee, Integer> {

    boolean isExistByUsername(String username);
}

