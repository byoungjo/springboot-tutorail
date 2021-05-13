package com.batchdev.multiDbTest.dao;

import com.batchdev.multiDbTest.model.Employ;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("OracleDao")
public class OracleDao {
    @Autowired
    @Qualifier("oracleSqlSessionTemplate")
    private SqlSessionTemplate sqlSession;

    public Employ findModelByModelId(String empId) {
        return sqlSession.selectOne("employMapper.selectEmployById", empId);
    }
//
//    public int saveModel(Employ model) {
//        return sqlSession.insert("model.saveModel", model);
//    }
}