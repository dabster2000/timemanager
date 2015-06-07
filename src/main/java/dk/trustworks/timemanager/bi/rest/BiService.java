package dk.trustworks.timemanager.bi.rest;

import dk.trustworks.timemanager.bi.model.ProjectYearEconomy;
import dk.trustworks.timemanager.client.model.Client;
import dk.trustworks.timemanager.client.model.Project;
import dk.trustworks.timemanager.bi.logic.BudgetCalculator;
import dk.trustworks.timemanager.client.dto.Budget;
import dk.trustworks.timemanager.bi.dto.ReportDTO;
import dk.trustworks.timemanager.bi.logic.BudgetSummary;
import dk.trustworks.timemanager.client.model.Task;
import dk.trustworks.timemanager.client.model.TaskWorkerConstraint;
import dk.trustworks.timemanager.client.repos.*;
import dk.trustworks.timemanager.time.model.Work;
import dk.trustworks.timemanager.time.repos.WorkRepository;
import dk.trustworks.timemanager.user.model.User;
import dk.trustworks.timemanager.user.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/bimanager/request")
public class BiService {

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
	
	@Autowired
	private BudgetSummary budgetSummary;
	
	@Autowired
	private BudgetCalculator budgetCalculator;

    @RequestMapping(value = "/getProjectYearBudgets", method = RequestMethod.GET)
    @Transactional(readOnly=true)
    public ProjectYearEconomy[] getProjectYearBudgets(@RequestParam("year") int year) {
		long timer = System.currentTimeMillis();

        List<ProjectYearEconomy> budgets = budgetSummary.calcProjectYearBudgets(year);
		ProjectYearEconomy[] economies = budgets.toArray(new ProjectYearEconomy[budgets.size()]);
        return economies;
    }

    @RequestMapping(value = "/getProjectYearActual", method = RequestMethod.GET)
    @Transactional(readOnly=true)
    public ProjectYearEconomy[] getProjectYearActual(@RequestParam("year") int year) {
		long timer = System.currentTimeMillis();

    	Hashtable<String, ProjectYearEconomy> budget = budgetSummary.calcProjectYearActual(year);
        ProjectYearEconomy[] economies = budget.values().toArray(new ProjectYearEconomy[budget.values().size()]);
    	return economies;
    }

    @RequestMapping(value = "/getUserProjectYearBudgets", method = RequestMethod.GET)
    @Transactional(readOnly=true)
    public ProjectYearEconomy[] getUserProjectYearBudgets(@RequestParam("userUUID") String userUUID, @RequestParam("year") int year) {
		long timer = System.currentTimeMillis();

    	Map<User, List<ProjectYearEconomy>> userProjectYearBudgets = budgetSummary.calcUserProjectYearBudgets(userUUID, year);
    	ProjectYearEconomy[] economies = userProjectYearBudgets.get(userRepository.findOne(userUUID)).toArray(new ProjectYearEconomy[userProjectYearBudgets.get(userRepository.findOne(userUUID)).size()]);
    	return economies;
    }

    @RequestMapping(value = "/getUserProjectYearActual", method = RequestMethod.GET)
    @Transactional(readOnly=true)
    public ProjectYearEconomy[] getUserProjectYearActual(@RequestParam("userUUID") String userUUID, @RequestParam("year") int year) {
		long timer = System.currentTimeMillis();

    	Map<User, List<ProjectYearEconomy>> userProjectYearActual = budgetSummary.calcUserProjectYearActual(userUUID, year);
    	ProjectYearEconomy[] economies = userProjectYearActual.get(userRepository.findOne(userUUID)).toArray(new ProjectYearEconomy[userProjectYearActual.get(userRepository.findOne(userUUID)).size()]);
    	return economies;
    }

    @RequestMapping(value = "/getUserProjectYearTimeBudgets", method = RequestMethod.GET)
    @Transactional(readOnly=true)
    public ProjectYearEconomy[] getUserProjectYearTimeBudgets(@RequestParam("userUUID") String userUUID, @RequestParam("year") int year) {
		long timer = System.currentTimeMillis();

    	Map<User, List<ProjectYearEconomy>> userProjectYearBudgets = budgetSummary.calcUserProjectYearTimeBudgets(userUUID, year);
    	ProjectYearEconomy[] economies = userProjectYearBudgets.get(userRepository.findOne(userUUID)).toArray(new ProjectYearEconomy[userProjectYearBudgets.get(userRepository.findOne(userUUID)).size()]);
    	return economies;
    }

    @RequestMapping(value = "/getUserProjectYearTimeActual", method = RequestMethod.GET)
    @Transactional(readOnly=true)
    public ProjectYearEconomy[] getUserProjectYearTimeActual(@RequestParam("userUUID") String userUUID, @RequestParam("year") int year) {
		long timer = System.currentTimeMillis();

    	Map<User, List<ProjectYearEconomy>> userProjectYearActual = budgetSummary.calcUserProjectYearTimeActual(userUUID, year);
    	ProjectYearEconomy[] economies = userProjectYearActual.get(userRepository.findOne(userUUID)).toArray(new ProjectYearEconomy[userProjectYearActual.get(userRepository.findOne(userUUID)).size()]);
    	return economies;
    }
    
    @RequestMapping(value = "/getReport", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    public ArrayList<ReportDTO> getReport(@RequestParam("month") int month, @RequestParam("year") int year) {
        ArrayList<ReportDTO> reportDTOs = new ArrayList<>();
        
        for (Work work : workRepository.findByYearAndMonth(year, month)) {
            ReportDTO reportDTO = null;
            for (ReportDTO newReportDTO : reportDTOs) {
                if(newReportDTO.getTaskUUID().equals(work.getTaskUUID()) && newReportDTO.getWorkerUUID().equals(work.getUserUUID())) {
                    reportDTO = newReportDTO;
                }
            }
            if(reportDTO == null) {
                reportDTO = new ReportDTO();
                if(work.getWorkDuration() > 0) reportDTOs.add(reportDTO);
                reportDTO.setWorkerName(userRepository.findOne(work.getUserUUID()).getFirstname() + " " + userRepository.findOne(work.getUserUUID()).getLastname());
                reportDTO.setWorkerUUID(work.getUserUUID());
                Task task = taskRepository.findOne(work.getTaskUUID());
				Project project = projectRepository.findOne(task.getProjectUUID());
				Client client = clientRepository.findOne(project.getClientUUID());
				reportDTO.setClientName(client.getName()); 
                reportDTO.setProjectName(project.getName());
                reportDTO.setTaskName(task.getName());
                reportDTO.setTaskUUID(work.getTaskUUID());
            }

            reportDTO.setHours(reportDTO.getHours() + work.getWorkDuration());

            for (TaskWorkerConstraint taskWorkerConstraint : taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(work.getTaskUUID(), work.getUserUUID())) {
                reportDTO.setRate(taskWorkerConstraint.getPrice());
                reportDTO.setSum(reportDTO.getHours() * reportDTO.getRate());
            }
        }

        return reportDTOs;
    }
    
    @RequestMapping(value = "/getProjectStatus", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    public ArrayList<Budget> getProjectStatus(@RequestParam("projectUUID") String projectUUID) {
    	ArrayList<Budget> budgets = new ArrayList<>();
    	for (Task task : taskRepository.findByProjectUUID(projectUUID)) {
			Budget remainingTaskBudget = budgetCalculator.calculateRemainingTaskBudget(task.getUUID());
			budgets.add(remainingTaskBudget);
		}
    	return budgets;
    }
}
