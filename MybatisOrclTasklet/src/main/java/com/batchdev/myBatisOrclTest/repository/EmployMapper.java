package com.batchdev.myBatisOrclTest.repository;

import com.batchdev.myBatisOrclTest.model.Employ;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface EmployMapper {
    Employ selectEmployById(String id);
}
