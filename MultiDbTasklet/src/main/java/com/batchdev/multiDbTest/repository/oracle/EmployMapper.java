package com.batchdev.multiDbTest.repository.oracle;

import com.batchdev.multiDbTest.model.Employ;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployMapper {
    Employ selectEmployById(String id);
}
