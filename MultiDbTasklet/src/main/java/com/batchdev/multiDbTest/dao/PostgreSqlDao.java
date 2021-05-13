package com.batchdev.multiDbTest.dao;

import com.batchdev.multiDbTest.model.Bill;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("PostgreSqlDao")
public class PostgreSqlDao {
    @Autowired
    @Qualifier("postgreSqlSqlSessionTemplate")
    private SqlSessionTemplate sqlSession;

    public Bill findModelByModelId(long modelId) {
        return sqlSession.selectOne("BillMapper.findModelByModelId", modelId);
    }

//    public int saveModel(Bill model) {
//        return sqlSession.insert("model.saveModel", model);
//    }
}