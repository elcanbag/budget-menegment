package com.example.budgetmanager.repository;

import com.example.budgetmanager.dto.MonthlyReportDTO;
import com.example.budgetmanager.model.ExpenseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRecordRepository extends JpaRepository<ExpenseRecord, Long> {
    List<ExpenseRecord> findByUserUsername(String username);
    Optional<ExpenseRecord> findByIdAndUserUsername(Long id, String username);
    List<ExpenseRecord> findByUserUsernameAndDateBetween(String username, LocalDateTime start, LocalDateTime end);


    @Query("select sum(er.amount) from ExpenseRecord er where er.user.username = ?1")
    Double getTotalExpenseByUsername(String username);

    @Query("select new com.example.budgetmanager.dto.MonthlyReportDTO(" +
            "CONCAT(FUNCTION('YEAR', er.date), '-', FUNCTION('MONTH', er.date)), " +
            "0, sum(er.amount)) " +
            "from ExpenseRecord er where er.user.username = ?1 group by FUNCTION('YEAR', er.date), FUNCTION('MONTH', er.date)")
    List<MonthlyReportDTO> getMonthlyExpenseByUsername(String username);

    @Query("SELECT er.category.name, SUM(er.amount) " +
            "FROM ExpenseRecord er " +
            "WHERE er.user.username = :username " +
            "GROUP BY er.category.name")
    List<Object[]> getTotalExpenseGroupedByCategory(String username);

    @Query("SELECT er.category.name, SUM(er.amount) " +
            "FROM ExpenseRecord er " +
            "WHERE er.user.username = :username " +
            "AND er.date BETWEEN :start AND :end " +
            "GROUP BY er.category.name")
    List<Object[]> getExpenseByCategoryInDateRange(String username, LocalDateTime start, LocalDateTime end);

}
