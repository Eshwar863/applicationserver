package com.example.application.Config;

import com.example.application.Entity.Amounts;
import com.example.application.Service.AmountService;
import com.example.application.Service.LoanAmountService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@EnableScheduling
@Configuration
public class SchedulerConfig {
    final
    AmountService amountService;
    final
    LoanAmountService loanAmountService;

    public SchedulerConfig(AmountService amountService, LoanAmountService loanAmountService) {
        this.amountService = amountService;
        this.loanAmountService = loanAmountService;
    }


    //@Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron ="0 0 * * * *")
    //@Scheduled(cron = "0 * * ? * * ")
    @Scheduled(cron = "0 0 */6 * * * ")
    public void scheduleMonthlyInterestUpdate() {
        loanAmountService.updateInterestForAllLoans();
    }

    @Scheduled(cron ="0 05 0 1 * *")
    public  void Update(){
        // updates Every Month Total amounts and
        amountService.update();
    }


    //    @Scheduled(cron ="0 0 * * * *")
    //@Scheduled(cron ="0 0 0 1,15 * ?")


    @Scheduled(cron = "0 10 0 1 * ?")
    public Amounts GetByMonth(){
        return amountService.getByDate();
    }

}
