package com.example.budgetmanager.repository;

import com.example.budgetmanager.dto.MonthlyReportDTO;
import com.example.budgetmanager.model.ExpenseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRecordRepository extends JpaRepository<ExpenseRecord, Long> {
    List<ExpenseRecord> findByUserUsername(String username);
    Optional<ExpenseRecord> findByIdAndUserUsername(Long id, String username);


    @Query("select sum(er.amount) from ExpenseRecord er where er.user.username = ?1")
    Double getTotalExpenseByUsername(String username);

    @Query("select new com.example.budgetmanager.dto.MonthlyReportDTO(" +
            "CONCAT(FUNCTION('YEAR', er.date), '-', FUNCTION('MONTH', er.date)), " +
            "0, sum(er.amount)) " +
            "from ExpenseRecord er where er.user.username = ?1 group by FUNCTION('YEAR', er.date), FUNCTION('MONTH', er.date)")
    List<MonthlyReportDTO> getMonthlyExpenseByUsername(String username);
}
