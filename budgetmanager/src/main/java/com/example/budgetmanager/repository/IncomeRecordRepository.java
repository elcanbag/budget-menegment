package com.example.budgetmanager.repository;

import com.example.budgetmanager.dto.MonthlyReportDTO;
import com.example.budgetmanager.model.IncomeRecord;
import com.example.budgetmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IncomeRecordRepository extends JpaRepository<IncomeRecord, Long> {

    List<IncomeRecord> findByUser(User user);

    Optional<IncomeRecord> findByIdAndUser(Long id, User user);


    @Query("select sum(ir.amount) from IncomeRecord ir where ir.user.username = ?1")
    Double getTotalIncomeByUsername(String username);

    @Query("select new com.example.budgetmanager.dto.MonthlyReportDTO(" +
            "CONCAT(FUNCTION('YEAR', ir.date), '-', FUNCTION('MONTH', ir.date)), " +
            "sum(ir.amount), 0) " +
            "from IncomeRecord ir where ir.user.username = ?1 group by FUNCTION('YEAR', ir.date), FUNCTION('MONTH', ir.date)")
    List<MonthlyReportDTO> getMonthlyIncomeByUsername(String username);
}
