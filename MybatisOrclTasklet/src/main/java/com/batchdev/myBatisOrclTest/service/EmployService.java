package com.batchdev.myBatisOrclTest.service;

import com.batchdev.myBatisOrclTest.model.Employ;
import com.batchdev.myBatisOrclTest.repository.EmployMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployService {

    @Autowired
    private EmployMapper employMapper;

    public Employ getEmploy(String empId ) {
        return employMapper.selectEmployById(empId);
    }
}
