package dk.trustworks.timemanager.bi.logic;

import dk.trustworks.timemanager.client.model.Project;
import dk.trustworks.timemanager.client.repos.*;
import dk.trustworks.timemanager.time.model.Work;
import dk.trustworks.timemanager.user.repos.UserRepository;
import dk.trustworks.timemanager.client.dto.Budget;
import dk.trustworks.timemanager.client.model.MonthBudget;
import dk.trustworks.timemanager.client.model.Task;
import dk.trustworks.timemanager.client.model.TaskWorkerConstraint;
import dk.trustworks.timemanager.time.repos.WeekRepository;
import dk.trustworks.timemanager.time.repos.WorkRepository;
import dk.trustworks.timemanager.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BudgetCalculator {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WeekRepository weekRepository;
	
	@Autowired
	private TaskWorkerConstraintRepository taskWorkerConstraintRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private MonthBudgetRepository monthBudgetRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private WorkRepository workRepository;

    @Cacheable("projectMonthlyBudget")
	public Budget calculateProjectMonthlyBudget(String projectUUID) {
		Project project = projectRepository.findOne(projectUUID);
		double total = 0.0;

		for(MonthBudget monthBudget : monthBudgetRepository.findByProjectUUID(projectUUID)) {
			total += monthBudget.getBudget();
		}
		return new Budget(project.getBudget() - total);
	}
    
    @Cacheable("projectBudget")
	public Budget calculateRemainingProjectBudget(String projectUUID) {
		Project project = projectRepository.findOne(projectUUID);
		double total = 0.0;

		for(Task task : taskRepository.findByProjectUUID(projectUUID)) {
		    Map<String, TaskWorkerConstraint> twcMap = new HashMap<>();
		    for (TaskWorkerConstraint taskWorkerConstraint : taskWorkerConstraintRepository.findByTaskUUID(task.getUUID())) {
		        twcMap.put(taskWorkerConstraint.getUserUUID(), taskWorkerConstraint);
		    }
		    for (Work work : workRepository.findByTaskUUID(task.getUUID())) {
		        total += work.getWorkDuration() * (twcMap.get(work.getUserUUID()).getPrice());
		    }
		}
		return new Budget(project.getBudget() - total);
	}

    @Cacheable("taskWorkerBudget")
	public Budget calculateRemainingTaskWorkerBudget(String taskUUID, String userUUID) {
		TaskWorkerConstraint taskWorkerConstraint = null;
		for (TaskWorkerConstraint twc : taskWorkerConstraintRepository.findByTaskUUID(taskUUID)) {
		    if(twc.getUserUUID().equals(userUUID)) taskWorkerConstraint = twc;
		}
		if(taskWorkerConstraint==null) return new Budget(0.0);

		Double remainingBudget = (taskWorkerConstraint!=null)?taskWorkerConstraint.getBudget():0.0; // if there are no taskWorkerConstraints, budget must be 0.0
		for (Work work : workRepository.findByTaskUUID(taskUUID)) {
		    if(!work.getUserUUID().equals(userUUID)) continue;
		    remainingBudget -= work.getWorkDuration() * taskWorkerConstraint.getPrice();
		}
		return new Budget(remainingBudget);
	}
    
    public Budget calculateRemainingTaskBudget(String taskUUID) {
    	Task task = taskRepository.findOne(taskUUID);
    	Budget budget = new Budget();
    	budget.setBudget(task.getBudget());
    	budget.setName(task.getName());
    	for (User user : userRepository.findAll()) {
			Budget remainingTaskWorkerBudget = calculateRemainingTaskWorkerBudget(taskUUID, user.getUUID());
			budget.setRemainingBudget(budget.getRemainingBudget() + remainingTaskWorkerBudget.getRemainingBudget());
		}
    	budget.setUsedBudget(task.getBudget() - budget.getRemainingBudget());
    	return budget;
    }
}
