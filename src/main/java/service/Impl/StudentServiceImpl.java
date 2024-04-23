package service.Impl;

import base.service.BaseServiceImpl;
import domain.Student;
import org.hibernate.SessionFactory;
import repository.StudentRepository;
import service.StudentService;

public class StudentServiceImpl extends BaseServiceImpl<Student, Integer, StudentRepository> implements StudentService {

    public StudentServiceImpl(StudentRepository repository ) {
        super ( repository );
    }
}