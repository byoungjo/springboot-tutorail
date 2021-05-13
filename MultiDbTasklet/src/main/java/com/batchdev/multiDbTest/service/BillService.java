package com.batchdev.multiDbTest.service;

import com.batchdev.multiDbTest.dao.PostgreSqlDao;
import com.batchdev.multiDbTest.model.Bill;
import com.batchdev.multiDbTest.repository.postgreSql.BillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BillService {
    @Autowired
    @Qualifier("postgreSqlDao")
    private PostgreSqlDao postgreSqlDao;

    @Autowired
    private BillMapper billMapper;

    public Bill getBill(long id) {
        return postgreSqlDao.findModelByModelId(id);
    }
//    public List<Bill> getBillList() {
//        return postgreSqlDao.
//    }
}
