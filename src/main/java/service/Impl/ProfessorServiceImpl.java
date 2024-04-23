package service.Impl;

import base.service.BaseServiceImpl;
import domain.Professor;
import org.hibernate.SessionFactory;
import repository.ProfessorRepository;
import service.ProfessorService;

public class ProfessorServiceImpl extends BaseServiceImpl<Professor, Integer, ProfessorRepository> implements ProfessorService {


    public ProfessorServiceImpl(ProfessorRepository repository ) {
        super ( repository );
    }
}
