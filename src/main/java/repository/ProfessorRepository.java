package repository;

import base.repository.BaseRepository;
import domain.Professor;

public interface ProfessorRepository extends BaseRepository<Professor, Integer> {

    boolean isExistByUsername(String username);
}

