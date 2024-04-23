package repository.Impl;

import base.repository.BaseRepositoryImpl;
import domain.Professor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repository.ProfessorRepository;

import java.util.Optional;

public class ProfessorRepositroyImpl extends BaseRepositoryImpl<Professor, Integer> implements ProfessorRepository {


    public ProfessorRepositroyImpl(Session session) {
        super ( session );
    }

    @Override
    public Class<Professor> getEntityClass() {
        return Professor.class;
    }

    @Override
    public boolean isExistByUsername(String username) {
        return ((session.createQuery ( "SELECT count(e) FROM " + entityClass.getSimpleName () + " e", Long.class )
                .getSingleResult ())>0);
    }

}




