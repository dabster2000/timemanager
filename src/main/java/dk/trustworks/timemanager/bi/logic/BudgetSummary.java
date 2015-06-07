package dk.trustworks.timemanager.bi.logic;

import dk.trustworks.timemanager.bi.model.ProjectYearEconomy;
import dk.trustworks.timemanager.time.model.Work;
import dk.trustworks.timemanager.time.repos.WorkRepository;
import dk.trustworks.timemanager.user.model.User;
import dk.trustworks.timemanager.user.repos.UserRepository;
import dk.trustworks.timemanager.client.model.*;
import dk.trustworks.timemanager.client.repos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class BudgetSummary {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WorkRepository workRepository;
	
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskWorkerConstraintRepository taskWorkerConstraintRepository;
	
	@Autowired
	private MonthBudgetRepository monthBudgetRepository;
	
	@Autowired
	private MonthBudgetHistoryRepository monthBudgetHistoryRepository;
	
	@Cacheable("projectYearBudgets")
	public List<ProjectYearEconomy> calcProjectYearBudgets(int year) {
		List<ProjectYearEconomy> projectYearBudgets = new ArrayList<ProjectYearEconomy>();
		
		StreamSupport.stream(projectRepository.findAll().spliterator(), true).map((project) -> {
			ProjectYearEconomy budgetSummary = new ProjectYearEconomy(project.getUUID(), project.getName());

			for (int month = 0; month < 12; month++) {
				List<MonthBudgetHistory> monthBudgetHistory = monthBudgetHistoryRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(), year, month);
				if(monthBudgetHistory.size() == 0) {
					List<MonthBudget> monthBudget = monthBudgetRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(),  year, month);
					if(monthBudget.size() == 0) continue;
					budgetSummary.getAmount()[month] = monthBudget.get(0).getBudget();
				} else {
					budgetSummary.getAmount()[month] = monthBudgetHistory.get(0).getBudget();
				}
				
			}
			return budgetSummary;
			
		}).forEach(result -> projectYearBudgets.add(result));
		
		/*
		for (Project project : projectRepository.findAll()) {
			ProjectYearEconomy budgetSummary = new ProjectYearEconomy(project.getUUID(), project.getName());

			for (int month = 0; month < 12; month++) {
				List<MonthBudgetHistory> monthBudgetHistory = monthBudgetHistoryRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(), year, month);
				if(monthBudgetHistory.size() == 0) {
					log.info("monthBudgetHistory.size(): "+monthBudgetHistory.size());
					List<MonthBudget> monthBudget = monthBudgetRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(),  year, month);
					if(monthBudget.size() == 0) continue;
					log.info("monthBudget.get(0).getBudget(): "+monthBudget.get(0).getBudget());
					budgetSummary.getAmount()[month] = monthBudget.get(0).getBudget();
				} else {
					log.info("monthBudgetHistory.size(): "+monthBudgetHistory.size());
					log.info("monthBudgetHistory.get(0).getBudget(): "+monthBudgetHistory.get(0).getBudget());
					budgetSummary.getAmount()[month] = monthBudgetHistory.get(0).getBudget();
				}
				
			}
			
			projectYearBudgets.add(budgetSummary);
		}
		*/

		return projectYearBudgets;
	}
	
	@Cacheable("projectYearActual")
    public Hashtable<String, ProjectYearEconomy> calcProjectYearActual(int year) {
		Hashtable<String, Project> taskProjects = buildTaskProjects();
    	
    	Hashtable<String, ProjectYearEconomy> container = new Hashtable<String, ProjectYearEconomy>();
		Collection<Work> workYear = workRepository.findByYear(year);
		for (Work work : workYear) {
			ProjectYearEconomy projectYearEconomy = null;
			try {
				projectYearEconomy = container.get(taskProjects.get(work.getTaskUUID()).getUUID());
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			if(projectYearEconomy==null) {
				projectYearEconomy = new ProjectYearEconomy(taskProjects.get(work.getTaskUUID()).getUUID(), taskProjects.get(work.getTaskUUID()).getName());
				container.put(taskProjects.get(work.getTaskUUID()).getUUID(), projectYearEconomy);
			}
			projectYearEconomy.getAmount()[work.getMonth()] += work.getWorkDuration() * taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(work.getTaskUUID(), work.getUserUUID()).iterator().next().getPrice();
		}
		return container;
	}
	
	@Cacheable("projectYearUserBudgets")
	public Map<User, List<ProjectYearEconomy>> calcUserProjectYearBudgets(String userUUID, int year) {
    	Map<User, List<ProjectYearEconomy>> userProjectYearBudgets = new HashMap<User, List<ProjectYearEconomy>>();
    	//for(User user : userRepository.findOne(userUUID)) {
    	User user = userRepository.findOne(userUUID);
		if(userProjectYearBudgets.get(user) == null) userProjectYearBudgets.put(user, new ArrayList<ProjectYearEconomy>());
		else userProjectYearBudgets.get(user).clear();
		
		for (Project project : projectRepository.findAll()) {
			double userSum = 0.0;
			for(Task task : taskRepository.findByProjectUUID(project.getUUID())) {
				for(TaskWorkerConstraint taskWorkerConstraint : taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(task.getUUID(), user.getUUID())) {
					userSum += taskWorkerConstraint.getBudget();
				}
			}

			double userPercentageOfProjectBudget = 0.0;
			if(userSum > 0.0) userPercentageOfProjectBudget = userSum / project.getBudget(); 
			else continue;
			
			ProjectYearEconomy budgetSummary = new ProjectYearEconomy(project.getUUID(), project.getName());
			
			for (int month = 0; month < 12; month++) {
				List<MonthBudgetHistory> monthBudgetHistory = monthBudgetHistoryRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(), year, month);
				if(monthBudgetHistory.size() == 0) {
					List<MonthBudget> monthBudget = monthBudgetRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(),  year, month);
					if(monthBudget.size() == 0) continue;
					budgetSummary.getAmount()[month] = monthBudget.get(0).getBudget() * userPercentageOfProjectBudget;
				} else {
					budgetSummary.getAmount()[month] = monthBudgetHistory.get(0).getBudget() * userPercentageOfProjectBudget;
				}
				
			}
			userProjectYearBudgets.get(user).add(budgetSummary);
		}
		//}
		return userProjectYearBudgets;
	}
	
	@Cacheable("projectYearUserTimeBudgets")
	public Map<User, List<ProjectYearEconomy>> calcUserProjectYearTimeBudgets(String userUUID, int year) {
    	Map<User, List<ProjectYearEconomy>> userProjectYearTimeBudgets = new HashMap<>();
    	//for(User user : userRepository.findAll()) {
    	User user = userRepository.findOne(userUUID);
		if(userProjectYearTimeBudgets.get(user) == null) userProjectYearTimeBudgets.put(user, new ArrayList<>());
		else userProjectYearTimeBudgets.get(user).clear();
		
		StreamSupport.stream(projectRepository.findAll().spliterator(), true).map((project) -> {
			// For each project get the users share of the project-budget in hours
			double userSum = 0.0;
			for(Task task : taskRepository.findByProjectUUID(project.getUUID())) {
				for(TaskWorkerConstraint taskWorkerConstraint : taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(task.getUUID(), user.getUUID())) {
					userSum += taskWorkerConstraint.getBudget() / taskWorkerConstraint.getPrice();
				}
			}
			
			// Calculate the users share of the total project-budget in percentage.
			// First calculate the total amount of hours in the project.
			double amountOfHoursInProject = 0.0;
			for(User otherUser : userRepository.findAll()) {
				for(Task task : taskRepository.findByProjectUUID(project.getUUID())) {
					for(TaskWorkerConstraint taskWorkerConstraint : taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(task.getUUID(), otherUser.getUUID())) {
						if(taskWorkerConstraint.getPrice() > 0.0) amountOfHoursInProject += taskWorkerConstraint.getBudget() / taskWorkerConstraint.getPrice();
					}
				}
			}
			if(amountOfHoursInProject <= 0.0 || amountOfHoursInProject == Double.NaN) return null;
			if(userSum <= 0.0 || userSum == Double.NaN) return null;
			
			double userPercentageOfProjectBudget = 0.0;
			if(userSum > 0.0) userPercentageOfProjectBudget = userSum / amountOfHoursInProject; 
			else return null;
			
			ProjectYearEconomy budgetSummary = new ProjectYearEconomy(project.getUUID(), project.getName());
			
			for (int month = 0; month < 12; month++) {
				List<MonthBudgetHistory> monthBudgetHistory = monthBudgetHistoryRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(), year, month);
				if(monthBudgetHistory.size() == 0) {
					List<MonthBudget> monthBudget = monthBudgetRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(),  year, month);
					if(monthBudget.size() == 0) continue;
					budgetSummary.getAmount()[month] = ((monthBudget.get(0).getBudget() * userPercentageOfProjectBudget) / project.getBudget()) * amountOfHoursInProject;
				} else {
					budgetSummary.getAmount()[month] = ((monthBudgetHistory.get(0).getBudget() * userPercentageOfProjectBudget) / project.getBudget()) * amountOfHoursInProject;
				}
				
			}
			return budgetSummary;
		}).filter(result -> result!=null).forEach(result -> userProjectYearTimeBudgets.get(user).add(result));
		
		/*
		for (Project project : projectRepository.findAll()) {
			// For each project get the users share of the project-budget in hours
			double userSum = 0.0;
			for(Task task : taskRepository.findByProjectUUID(project.getUUID())) {
				for(TaskWorkerConstraint taskWorkerConstraint : taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(task.getUUID(), user.getUUID())) {
					userSum += taskWorkerConstraint.getBudget() / taskWorkerConstraint.getPrice();
				}
			}
			log.info("userSum<"+user.getLastname()+", "+project.getName()+">: "+ userSum);
			
			// Calculate the users share of the total project-budget in percentage.
			// First calculate the total amount of hours in the project.
			double amountOfHoursInProject = 0.0;
			for(User otherUser : userRepository.findAll()) {
				for(Task task : taskRepository.findByProjectUUID(project.getUUID())) {
					for(TaskWorkerConstraint taskWorkerConstraint : taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(task.getUUID(), otherUser.getUUID())) {
						if(taskWorkerConstraint.getPrice() > 0.0) amountOfHoursInProject += taskWorkerConstraint.getBudget() / taskWorkerConstraint.getPrice();
					}
				}
			}
			log.info("amountOfHoursInProject: "+amountOfHoursInProject);
			if(amountOfHoursInProject <= 0.0 || amountOfHoursInProject == Double.NaN) continue;
			if(userSum <= 0.0 || userSum == Double.NaN) continue;
			
			double userPercentageOfProjectBudget = 0.0;
			if(userSum > 0.0) userPercentageOfProjectBudget = userSum / amountOfHoursInProject; 
			else continue;
			
			ProjectYearEconomy budgetSummary = new ProjectYearEconomy(project.getUUID(), project.getName());
			
			for (int month = 0; month < 12; month++) {
				List<MonthBudgetHistory> monthBudgetHistory = monthBudgetHistoryRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(), year, month);
				if(monthBudgetHistory.size() == 0) {
					List<MonthBudget> monthBudget = monthBudgetRepository.findByProjectUUIDAndYearAndMonth(project.getUUID(),  year, month);
					if(monthBudget.size() == 0) continue;
					budgetSummary.getAmount()[month] = ((monthBudget.get(0).getBudget() * userPercentageOfProjectBudget) / project.getBudget()) * amountOfHoursInProject;
				} else {
					budgetSummary.getAmount()[month] = ((monthBudgetHistory.get(0).getBudget() * userPercentageOfProjectBudget) / project.getBudget()) * amountOfHoursInProject;
					log.info(project.getName()+": (("+monthBudgetHistory.get(0).getBudget() + " * " + userPercentageOfProjectBudget + ") / " + project.getBudget() +") * "+amountOfHoursInProject+" = "+budgetSummary.getAmount()[month]);
				}
				
			}
			log.info("budgetSummary<"+user.getLastname()+">.getAmount().length: "+budgetSummary.getAmount().length);
			userProjectYearTimeBudgets.get(user).add(budgetSummary);
		}
		*/
		//}
		return userProjectYearTimeBudgets;
	}
	
	@Cacheable("projectYearUserActual")
	public Map<User, List<ProjectYearEconomy>> calcUserProjectYearActual(String userUUID, int year) {
		Hashtable<String, Project> taskProjects = buildTaskProjects();
    	
    	Map<User, List<ProjectYearEconomy>> userProjectYearActual = new HashMap<User, List<ProjectYearEconomy>>();
    	//for(User user : userRepository.findAll()) {
		User user = userRepository.findOne(userUUID);
		if(userProjectYearActual.get(user) == null) userProjectYearActual.put(user, new ArrayList<ProjectYearEconomy>());
		else userProjectYearActual.get(user).clear();
		
		Hashtable<String, ProjectYearEconomy> containerUser = new Hashtable<String, ProjectYearEconomy>();
		for (Work work : workRepository.findByYearAndUserUUID(year, user.getUUID())) {
			ProjectYearEconomy projectYearEconomy = null;
			try {
				projectYearEconomy = containerUser.get(taskProjects.get(work.getTaskUUID()).getUUID());
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			if(projectYearEconomy==null) {
				projectYearEconomy = new ProjectYearEconomy(taskProjects.get(work.getTaskUUID()).getUUID(), taskProjects.get(work.getTaskUUID()).getName());
				containerUser.put(taskProjects.get(work.getTaskUUID()).getUUID(), projectYearEconomy);
			}
			projectYearEconomy.getAmount()[work.getMonth()] += work.getWorkDuration() * taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(work.getTaskUUID(), work.getUserUUID()).iterator().next().getPrice();
		}
		userProjectYearActual.get(user).addAll(containerUser.values());
		//}
		return userProjectYearActual;
	}
	
	@Cacheable("projectYearUserTimeActual")
	public Map<User, List<ProjectYearEconomy>> calcUserProjectYearTimeActual(String userUUID, int year) {
		Hashtable<String, Project> taskProjects = buildTaskProjects();
    	
    	Map<User, List<ProjectYearEconomy>> userProjectYearActual = new HashMap<User, List<ProjectYearEconomy>>();
    	//for(User user : userRepository.findAll()) {
		User user = userRepository.findOne(userUUID);
		if(userProjectYearActual.get(user) == null) userProjectYearActual.put(user, new ArrayList<ProjectYearEconomy>());
		else userProjectYearActual.get(user).clear();
		
		Hashtable<String, ProjectYearEconomy> containerUser = new Hashtable<String, ProjectYearEconomy>();
		for (Work work : workRepository.findByYearAndUserUUID(year, user.getUUID())) {
			ProjectYearEconomy projectYearEconomy = null;
			try {
				projectYearEconomy = containerUser.get(taskProjects.get(work.getTaskUUID()).getUUID());
			} catch (NullPointerException e) {

			}
			if(projectYearEconomy==null) {
				projectYearEconomy = new ProjectYearEconomy(taskProjects.get(work.getTaskUUID()).getUUID(), taskProjects.get(work.getTaskUUID()).getName());
				containerUser.put(taskProjects.get(work.getTaskUUID()).getUUID(), projectYearEconomy);
			}
			projectYearEconomy.getAmount()[work.getMonth()] += work.getWorkDuration(); // * taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(work.getTaskUUID(), work.getUserUUID()).iterator().next().getPrice();
		}
		userProjectYearActual.get(user).addAll(containerUser.values());
		//}
		return userProjectYearActual;
	}

	private Hashtable<String, Project> buildTaskProjects() {
		Hashtable<String, Project> taskProjects = new Hashtable<String, Project>();
    	for (Project project : projectRepository.findAll()) {
			for (Task task : taskRepository.findByProjectUUID(project.getUUID())) {
				taskProjects.put(task.getUUID(), project);
			}
		}
		return taskProjects;
	}
	
}
