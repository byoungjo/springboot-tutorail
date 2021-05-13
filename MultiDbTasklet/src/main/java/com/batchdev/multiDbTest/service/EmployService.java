package com.batchdev.multiDbTest.service;

import com.batchdev.multiDbTest.dao.OracleDao;
import com.batchdev.multiDbTest.model.Employ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployService {
    @Autowired
    @Qualifier("oracleDao")
    private OracleDao oracleDao;

//    @Autowired
//    private EmployMapper employMapper;

    public Employ getEmploy(String empId ) {
        return oracleDao.findModelByModelId(empId);
    }
}
