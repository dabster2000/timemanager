package dk.trustworks.timemanager.jobs;

import dk.trustworks.timemanager.bi.logic.BudgetSummary;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@EnableScheduling
public class DataCollectorJob {
		
	@Autowired
	private BudgetSummary budgetSummary;
	
	//@Transactional(readOnly = true)
	//@Scheduled(initialDelay=10000, fixedRate=300000)
	public void work() {
		long timer = new Date().getTime();

		for (int year = 2013; year <= new DateTime().getYear() + 1; year++) {
			budgetSummary.calcProjectYearBudgets(year);
			budgetSummary.calcProjectYearActual(year);
		}
	}

}
