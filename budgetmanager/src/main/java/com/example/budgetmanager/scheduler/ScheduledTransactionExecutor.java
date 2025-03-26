package com.example.budgetmanager.scheduler;

import com.example.budgetmanager.service.ScheduledTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTransactionExecutor {

    private final ScheduledTransactionService scheduledTransactionService;


    @Scheduled(cron = "0 0 * * * *")
    public void executeScheduledTransactions() {
        scheduledTransactionService.executeScheduledTransactions();
    }
}
