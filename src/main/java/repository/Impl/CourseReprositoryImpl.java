package repository.Impl;

import base.repository.BaseRepositoryImpl;
import domain.Course;
import org.hibernate.Session;
import repository.CourseRepository;

public class CourseReprositoryImpl extends BaseRepositoryImpl<Course, Integer> implements CourseRepository {

    public CourseReprositoryImpl(Session session) {
        super ( session );
    }
    @Override
    public Class<Course> getEntityClass() {
        return Course.class;
    }

    @Override
    public Course findProfessorByUsername(String username) {
        return session.createQuery ("select c from Course c where c.professor.firstName = :username",
                Course.class)
                .setParameter ( "username", username )
                .getSingleResult();
    }
}
