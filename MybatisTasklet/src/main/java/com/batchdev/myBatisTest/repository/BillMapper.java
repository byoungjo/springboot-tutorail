package com.batchdev.myBatisTest.repository;

import com.batchdev.myBatisTest.model.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface BillMapper  {
    Bill selectBillById(Long id);
    List<Bill> selectAllBills();
    void insertBill(Bill bill);
    void updateBill(Bill bill);
}
