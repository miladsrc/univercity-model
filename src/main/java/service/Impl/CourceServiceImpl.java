package service.Impl;

import base.service.BaseServiceImpl;
import domain.Course;
import org.hibernate.SessionFactory;
import repository.CourseRepository;
import service.CourseService;

public class CourceServiceImpl extends BaseServiceImpl<Course, Integer, CourseRepository>
        implements CourseService {


    public CourceServiceImpl(CourseRepository repository ) {
        super ( repository );
    }
}
