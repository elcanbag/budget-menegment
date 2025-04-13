package com.example.budgetmanager.repository;

import com.example.budgetmanager.dto.MonthlyReportDTO;
import com.example.budgetmanager.model.IncomeRecord;
import com.example.budgetmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IncomeRecordRepository extends JpaRepository<IncomeRecord, Long> {

    List<IncomeRecord> findByUser(User user);
    Optional<IncomeRecord> findByIdAndUser(Long id, User user);
    List<IncomeRecord> findByUserUsername(String username);
    List<IncomeRecord> findByUserUsernameAndDateBetween(String username, LocalDateTime start, LocalDateTime end);


    @Query("select sum(ir.amount) from IncomeRecord ir where ir.user.username = ?1")
    Double getTotalIncomeByUsername(String username);

    @Query("select new com.example.budgetmanager.dto.MonthlyReportDTO(" +
            "CONCAT(FUNCTION('YEAR', ir.date), '-', FUNCTION('MONTH', ir.date)), " +
            "sum(ir.amount), 0) " +
            "from IncomeRecord ir where ir.user.username = ?1 group by CONCAT(FUNCTION('YEAR', ir.date), '-', FUNCTION('MONTH', ir.date))")
    List<MonthlyReportDTO> getMonthlyIncomeByUsername(String username);


    @Query("SELECT ir.category.name, SUM(ir.amount) " +
            "FROM IncomeRecord ir " +
            "WHERE ir.user.username = :username " +
            "AND ir.date BETWEEN :start AND :end " +
            "GROUP BY ir.category.name")
    List<Object[]> getIncomeByCategoryInDateRange(String username, LocalDateTime start, LocalDateTime end);

    List<IncomeRecord> findByUserAndDateLessThanEqual(User user, LocalDateTime dateTime);
}
