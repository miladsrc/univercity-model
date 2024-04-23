package service.Impl;

import base.service.BaseServiceImpl;
import domain.TermInformation;
import org.hibernate.SessionFactory;
import repository.TermInfoRepository;
import service.TermInfoService;

public class TermInfoServiceImpl extends BaseServiceImpl<TermInformation, Integer, TermInfoRepository> implements TermInfoService {

    public TermInfoServiceImpl(TermInfoRepository repository ) {
        super ( repository );
    }
}
