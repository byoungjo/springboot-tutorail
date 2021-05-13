package com.batchdev.multiDbTest.repository.postgreSql;

import com.batchdev.multiDbTest.model.Bill;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
//@Mapper
public interface BillMapper  {
    Bill selectBillById(Long id);
    List<Bill> selectAllBills();
    void insertBill(Bill bill);
    void updateBill(Bill bill);
}
