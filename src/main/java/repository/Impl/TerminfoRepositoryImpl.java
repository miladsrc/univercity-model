package repository.Impl;

import base.repository.BaseRepositoryImpl;
import domain.Course;
import domain.TermInformation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repository.TermInfoRepository;

import java.util.Optional;

public class TerminfoRepositoryImpl extends BaseRepositoryImpl<TermInformation, Integer> implements TermInfoRepository {

    public TerminfoRepositoryImpl(Session session) {
        super ( session );
    }

    @Override
    public Class<TermInformation> getEntityClass() {
        return TermInformation.class;
    }
}

