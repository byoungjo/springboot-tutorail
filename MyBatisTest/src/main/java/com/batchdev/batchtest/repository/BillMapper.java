package com.batchdev.batchtest.repository;

import com.batchdev.batchtest.model.Bill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BillMapper {
    Bill selectBillById(Long id);
    List<Bill> selectAllBills();
    void insertBill(Bill bill);
    void updateBill(Bill bill);
}
