package dk.trustworks.timemanager.client.rest;

import dk.trustworks.timemanager.client.commands.CreateClientCommand;
import dk.trustworks.timemanager.client.dto.Budget;
import dk.trustworks.timemanager.client.dto.BudgetContainer;
import dk.trustworks.timemanager.client.dto.MonthBudgetView;
import dk.trustworks.timemanager.user.model.User;
import dk.trustworks.timemanager.user.services.UserService;
import dk.trustworks.timemanager.client.commands.CreateTaskWorkerConstraintCommand;
import dk.trustworks.timemanager.client.dto.TaskWorkerConstraintView;
import dk.trustworks.timemanager.client.model.*;
import dk.trustworks.timemanager.client.repos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/clientmanager/command")
public class ClientService {
	
	@Autowired
	private UserService userService;
	
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
	private ClientDataRepository clientDataRepository;

    @RequestMapping(value = "/createClient", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    @CacheEvict(value = {"clients","client"}, allEntries = true)
    public void createClient(@RequestBody CreateClientCommand command) {
    	clientRepository.save(new Client(UUID.randomUUID().toString(), command.getName(), command.getContactName(), command.getCommonPrice(), true));
    }
    
    @RequestMapping(value = "/updateClient", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    @CacheEvict(value = {"clients","client"}, allEntries = true)
    public void updateClient(@RequestBody Client command) {
    	Client foundClient = clientRepository.findOne(command.getUUID());
        foundClient.setCommonPrice(command.getCommonPrice());
        foundClient.setContactName(command.getContactName());
        foundClient.setName(command.getName());
        foundClient.setActive(command.isActive());
        clientRepository.save(foundClient);
    }
    
    @RequestMapping(value = "/createProject", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    @CacheEvict(value = {"projectYearBudgets","projectYearActual","projectYearUserBudgets","projectYearUserActual","projectYearUserTimeBudgets","projectYearUserTimeActual","projects"}, allEntries=true)
    public void createProject(@RequestBody Project command) {
    	command.setUUID(UUID.randomUUID().toString());
        projectRepository.save(command);
    }
    
    @RequestMapping(value = "/updateProject", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    @CacheEvict(value = {"projectYearBudgets","projectYearActual","projectYearUserBudgets","projectYearUserActual","projectYearUserTimeBudgets","projectYearUserTimeActual","budget","projects","projectBudget","taskWorkerBudget","projectMonthlyBudget"}, allEntries=true)
    public void updateProject(@RequestBody Project project) {
    	Project foundProject = projectRepository.findOne(project.getUUID());
        foundProject.setBudget(project.getBudget());
        foundProject.setCommonPrice(project.getCommonPrice());
        foundProject.setCustomerReference(project.getCustomerReference());
        foundProject.setName(project.getName());
        foundProject.setClientDataUUID(project.getClientDataUUID());
        foundProject.setActive(project.isActive());
        projectRepository.save(foundProject);
    }
    
    @RequestMapping(value = "/updateClientData", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    //@CacheEvict(value = "clientData", allEntries = true)
    public void updateClientData(@RequestBody ClientData clientData) {
    	ClientData one = clientDataRepository.findOne(clientData.getUUID());
    	if(one!=null) {
    		one.setClientName(clientData.getClientName());
    		one.setCity(clientData.getCity());
    		one.setContactPerson(clientData.getContactPerson());
    		one.setCvr(clientData.getCvr());
    		one.setEAN(clientData.getEAN());
    		one.setOtherAddressInfo(clientData.getOtherAddressInfo());
    		one.setPostalCode(clientData.getPostalCode());
    		one.setStreetNameNumber(clientData.getStreetNameNumber());
    		one.setClientUUID(clientData.getClientUUID());
    		clientData = one;
    	}
    	clientDataRepository.save(clientData);
    }
    
    @RequestMapping(value = "/createTask", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    @CacheEvict(value = {"projectYearBudgets","projectYearActual","projectYearUserBudgets","projectYearUserActual","projectYearUserTimeBudgets","projectYearUserTimeActual","tasks","budget"}, allEntries = true)
    public void createTask(@RequestBody Task command) {
    	command.setUUID(UUID.randomUUID().toString());

        Task task = taskRepository.save(command);

        for (User user : userService.getAllUsers()) {
            TaskWorkerConstraint taskWorkerConstraint = new TaskWorkerConstraint(UUID.randomUUID().toString(), task.getCommonPrice(), 0.0, user.getUUID(), task.getUUID());
            taskWorkerConstraintRepository.save(taskWorkerConstraint);
        }
    }
    
    @RequestMapping(value = "/updateTask", method = RequestMethod.POST)
    @Transactional
    //@CacheEvict(value = "task", allEntries = true)
    @CacheEvict(value = {"projectYearBudgets","projectYearActual","projectYearUserBudgets","projectYearUserActual","projectYearUserTimeBudgets","projectYearUserTimeActual","tasks","budget"}, allEntries=true)
    public void updateTask(@RequestBody Task task) {
    	Task foundTask = taskRepository.findOne(task.getUUID());
        foundTask.setBudget(task.getBudget());
        foundTask.setCommonPrice(task.getCommonPrice());
        foundTask.setName(task.getName());
        taskRepository.save(foundTask);
    }
    
    @RequestMapping(value = "/getTaskWorkerConstraintView", method = RequestMethod.GET)
    @Transactional
    @Cacheable("taskWorkerConstraintViews")
    public TaskWorkerConstraintView[] getTaskWorkerConstraintView(@RequestParam("taskUUID") String taskUUID) {
    	List<TaskWorkerConstraintView> view = new ArrayList<>();
    	for (User user : userService.getAllUsers()) {
			List<TaskWorkerConstraint> taskWorkerConstraints = taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(taskUUID, user.getUUID());
			TaskWorkerConstraint taskWorkerConstraint;
			if(taskWorkerConstraints.size() == 0) {
				taskWorkerConstraint = taskWorkerConstraintRepository.save(new TaskWorkerConstraint(UUID.randomUUID().toString(), 0.0, 0.0, user.getUUID(), taskUUID));
			} else {
				taskWorkerConstraint = taskWorkerConstraints.get(0);
			}
			view.add(new TaskWorkerConstraintView(taskWorkerConstraint.getUUID(), user.getUUID(), user.getFirstname() + " " + user.getLastname(), taskWorkerConstraint.getPrice(), taskWorkerConstraint.getBudget()));
		}
        return view.toArray(new TaskWorkerConstraintView[view.size()]);
    }
    
    @RequestMapping(value = "/createTaskWorkerConstraint", method = RequestMethod.POST)
    @Transactional
    @CacheEvict(value = {"projectYearBudgets","projectYearActual","projectYearUserBudgets","projectYearUserActual","projectYearUserTimeBudgets","projectYearUserTimeActual","taskWorkerConstraintViews","taskWorkerConstraints","budget"}, allEntries=true)
    public void createTaskWorkerConstraint(@RequestBody CreateTaskWorkerConstraintCommand command) {
    	taskWorkerConstraintRepository.save(new TaskWorkerConstraint(UUID.randomUUID().toString(), command.getPrice(), command.getBudget(), command.getUserUUID(), command.getTaskUUID()));
    }
    
    @RequestMapping(value = "/updateTaskWorkerConstraint", method = RequestMethod.POST)
    @Transactional
    //@CacheEvict(value = "taskWorkerConstraint", allEntries = true)
    @CacheEvict(value = {"projectYearBudgets","projectYearActual","projectYearUserBudgets","projectYearUserActual","taskWorkerConstraintViews","taskWorkerConstraints","budget"}, allEntries=true)
    public void updateTaskWorkerConstraint(@RequestBody TaskWorkerConstraint taskWorkerConstraint) {
    	TaskWorkerConstraint foundTaskWorkerConstraint = taskWorkerConstraintRepository.findOne(taskWorkerConstraint.getUUID());
        foundTaskWorkerConstraint.setBudget(taskWorkerConstraint.getBudget());
        foundTaskWorkerConstraint.setPrice(taskWorkerConstraint.getPrice());
        taskWorkerConstraintRepository.save(foundTaskWorkerConstraint);
    }
    
    @RequestMapping(value = "/getProjectMonthlyBudgets", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    //@Cacheable("monthBudgets")
    public BudgetContainer[] getProjectMonthlyBudgets() {
    	List<BudgetContainer> budgetContainers = new ArrayList<>();
    	
    	Iterable<Project> projects = projectRepository.findAll();
    	for (Project project : projects) {
    		List<Budget> budgets = new ArrayList<>();
    		
			List<MonthBudget> monthBudgets = monthBudgetRepository.findByProjectUUID(project.getUUID());
			for (MonthBudget monthBudget : monthBudgets) {
				if(monthBudget.getYear() != 2014) continue;
				budgets.add(new Budget(new DateFormatSymbols().getMonths()[monthBudget.getMonth()], 0.0, 0.0, monthBudget.getBudget()));
			}
			
			budgetContainers.add(new BudgetContainer(project.getName(), budgets.toArray(new Budget[budgets.size()])));
		}
    	
    	return budgetContainers.toArray(new BudgetContainer[budgetContainers.size()]);
    }

    @RequestMapping(value = "/getMonthBudgets", method = RequestMethod.GET)
    @Transactional
    //@Cacheable("monthBudgets")
    public MonthBudgetView getMonthBudgets(@RequestParam("projectUUID") String projectUUID, @RequestParam("year") int year) {
    	boolean isFound = false;
        for (MonthBudget monthBudget : monthBudgetRepository.findByProjectUUIDAndYear(projectUUID, year)) {
            if(monthBudget.getYear() == year) isFound = true;
        }
        
        if(!isFound) {
            for(int i = 0; i < 12; i++) {
                monthBudgetRepository.save(new MonthBudget(UUID.randomUUID().toString(), year, i, 0.0, projectUUID));
            }
        }

        MonthBudgetView monthBudgetView = new MonthBudgetView();
        monthBudgetView.setProjectUUID(projectUUID);
        monthBudgetView.setYear(year);
        
        List<MonthBudget> monthBudgets = monthBudgetRepository.findByProjectUUIDAndYearOrderByMonthAsc(projectUUID, year);
        
        monthBudgetView.setJan(monthBudgets.get(0).getBudget());
        monthBudgetView.setFeb(monthBudgets.get(1).getBudget());
        monthBudgetView.setMar(monthBudgets.get(2).getBudget());
        monthBudgetView.setApr(monthBudgets.get(3).getBudget());
        monthBudgetView.setMay(monthBudgets.get(4).getBudget());
        monthBudgetView.setJun(monthBudgets.get(5).getBudget());
        monthBudgetView.setJul(monthBudgets.get(6).getBudget());
        monthBudgetView.setAug(monthBudgets.get(7).getBudget());
        monthBudgetView.setSep(monthBudgets.get(8).getBudget());
        monthBudgetView.setOct(monthBudgets.get(9).getBudget());
        monthBudgetView.setNov(monthBudgets.get(10).getBudget());
        monthBudgetView.setDec(monthBudgets.get(11).getBudget());
        
        return monthBudgetView;
    }
    
    @RequestMapping(value = "/addMonthBudgetsByYear", method = RequestMethod.GET)
    @Transactional
    @CacheEvict(value = {"monthBudgets","budget"}, allEntries = true)
    public void addMonthBudgetsByYear(@RequestParam("projectUUID") String projectUUID, @RequestParam("year") int year) {
    	
    }

    @RequestMapping(value = "/updateMonthBudgets", method = RequestMethod.POST)
    @Transactional
    //@CacheEvict(value = "monthBudgets", allEntries = true)
    @CacheEvict(value = {"projectYearBudgets","projectYearActual","projectYearUserBudgets","projectYearUserActual","projectYearUserTimeBudgets","projectYearUserTimeActual","monthBudgets","budget","projectMonthlyBudget","projectBudget","taskWorkerBudget"}, allEntries=true)
    public void updateMonthBudgets(@RequestBody MonthBudgetView monthBudgetView) {
    	List<MonthBudget> monthBudgets = monthBudgetRepository.findByProjectUUIDAndYearOrderByMonthAsc(monthBudgetView.getProjectUUID(), monthBudgetView.getYear());
        
        monthBudgets.get(0).setBudget(monthBudgetView.getJan());
        monthBudgets.get(1).setBudget(monthBudgetView.getFeb());
        monthBudgets.get(2).setBudget(monthBudgetView.getMar());
        monthBudgets.get(3).setBudget(monthBudgetView.getApr());
        monthBudgets.get(4).setBudget(monthBudgetView.getMay());
        monthBudgets.get(5).setBudget(monthBudgetView.getJun());
        monthBudgets.get(6).setBudget(monthBudgetView.getJul());
        monthBudgets.get(7).setBudget(monthBudgetView.getAug());
        monthBudgets.get(8).setBudget(monthBudgetView.getSep());
        monthBudgets.get(9).setBudget(monthBudgetView.getOct());
        monthBudgets.get(10).setBudget(monthBudgetView.getNov());
        monthBudgets.get(11).setBudget(monthBudgetView.getDec());
        monthBudgetRepository.save(monthBudgets.get(0));
        monthBudgetRepository.save(monthBudgets.get(1));
        monthBudgetRepository.save(monthBudgets.get(2));
        monthBudgetRepository.save(monthBudgets.get(3));
        monthBudgetRepository.save(monthBudgets.get(4));
        monthBudgetRepository.save(monthBudgets.get(5));
        monthBudgetRepository.save(monthBudgets.get(6));
        monthBudgetRepository.save(monthBudgets.get(7));
        monthBudgetRepository.save(monthBudgets.get(8));
        monthBudgetRepository.save(monthBudgets.get(9));
        monthBudgetRepository.save(monthBudgets.get(10));
        monthBudgetRepository.save(monthBudgets.get(11));
    }
}
