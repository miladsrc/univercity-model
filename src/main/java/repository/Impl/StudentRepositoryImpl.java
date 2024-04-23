package repository.Impl;
import base.repository.BaseRepositoryImpl;
import domain.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repository.StudentRepository;

import java.util.Optional;

public class StudentRepositoryImpl extends BaseRepositoryImpl<Student, Integer> implements StudentRepository {


    public StudentRepositoryImpl(Session session) {
        super ( session );
    }

    @Override
    public Class<Student> getEntityClass() {
        return Student.class;
    }

    @Override
    public boolean isExistByUsername(String username) {
        return ((session.createQuery ( "SELECT count(e) FROM " + entityClass.getSimpleName () + " e", Long.class )
                .getSingleResult ())>0);
    }

}
