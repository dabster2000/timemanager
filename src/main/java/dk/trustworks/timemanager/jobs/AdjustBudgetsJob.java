package dk.trustworks.timemanager.jobs;

import dk.trustworks.timemanager.client.model.*;
import dk.trustworks.timemanager.client.repos.*;
import dk.trustworks.timemanager.time.model.Work;
import dk.trustworks.timemanager.time.repos.WorkRepository;
import lombok.extern.log4j.Log4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Log4j
@Component
@EnableScheduling
public class AdjustBudgetsJob {

	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	TaskWorkerConstraintRepository taskWorkerConstraintRepository;
	
	@Autowired
	MonthBudgetRepository monthBudgetRepository;
	
	@Autowired
	MonthBudgetHistoryRepository monthBudgetHistoryRepository;
	
	@Autowired
	WorkRepository workRepository;

	//@CacheEvict({"monthBudgets"})
	@Transactional
	@Scheduled(initialDelay=5000, fixedRate=300000) //(cron = "0 0 7 * * MON")
	public void work() {
		long timer = System.currentTimeMillis();
		
		DateTime dateTime = new DateTime();
		
		Map<Integer, Collection<Work>> workAllYears = new HashMap<>();
		
		for (Project project : projectRepository.findAll()) { // Først finder jeg alle projekter
			for (MonthBudget monthBudget : monthBudgetRepository.findByProjectUUID(project.getUUID())) { // Så skal jeg kigge projekternes månedsbudgetter igennem måned for måned
				if(monthBudgetHistoryRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(), monthBudget.getYear(), monthBudget.getMonth()).size() > 0) continue;
				if(monthBudget.getYear() < dateTime.getYear() || (monthBudget.getYear() == dateTime.getYear() && monthBudget.getMonth() < (dateTime.getMonthOfYear() - 1))) {
					Collection<Work> workAllYear;
					if(workAllYears.containsKey(monthBudget.getYear())) {
						workAllYear = workAllYears.get(monthBudget.getYear());
					}
					else {
						workAllYear = workRepository.findByYear(monthBudget.getYear());
						workAllYears.put(monthBudget.getYear(), workAllYear);
					}
					double sum = 0.0;
					//log.info("Found MonthBudget: "+monthBudget);
					
					for (Task task : taskRepository.findByProjectUUID(project.getUUID())) {
						for (Work work : workAllYear) {
							if(work.getTaskUUID().equals(task.getUUID()) && work.getYear() == monthBudget.getYear() && work.getMonth() == monthBudget.getMonth()) {
								for (TaskWorkerConstraint taskWorkerConstraint : taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(work.getTaskUUID(), work.getUserUUID())) {
									if(taskWorkerConstraint.getUserUUID().equals(work.getUserUUID())) {
										sum += work.getWorkDuration() * taskWorkerConstraint.getPrice();
									}
								}
								
							}
						}
					}
					monthBudgetHistoryRepository.save(new MonthBudgetHistory(monthBudget));
					monthBudget.setBudget(sum);
				} else {
					//monthBudgetHistoryRepository.save(new MonthBudgetHistory(monthBudget));
				}
			}
			
		}
	}

}
