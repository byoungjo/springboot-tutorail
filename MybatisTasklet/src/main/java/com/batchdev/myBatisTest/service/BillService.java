package com.batchdev.myBatisTest.service;

import com.batchdev.myBatisTest.model.Bill;
import com.batchdev.myBatisTest.repository.BillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BillService {

    @Autowired
    private BillMapper billMapper;

    public Bill getBill(long id) {
        return billMapper.selectBillById(id);
    }
    public List<Bill> getBillList() {
        return billMapper.selectAllBills();
    }
}
