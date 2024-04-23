package repository;

import base.repository.BaseRepository;
import domain.Course;

public interface CourseRepository extends BaseRepository<Course, Integer> {

    Course findProfessorByUsername(String username);
}
 