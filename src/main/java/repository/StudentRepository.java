package repository;

import base.repository.BaseRepository;
import domain.Student;

public interface StudentRepository extends BaseRepository<Student, Integer> {
    boolean isExistByUsername(String username);
}
