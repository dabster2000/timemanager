package dk.trustworks.timemanager.time.rest;

import dk.trustworks.timemanager.bi.logic.BudgetCalculator;
import dk.trustworks.timemanager.client.commands.RegisterWorkCommand;
import dk.trustworks.timemanager.client.dto.Budget;
import dk.trustworks.timemanager.client.model.Task;
import dk.trustworks.timemanager.client.repos.*;
import dk.trustworks.timemanager.time.dto.TaskWeekView;
import dk.trustworks.timemanager.time.model.Week;
import dk.trustworks.timemanager.client.commands.AssignTaskToWeekCommand;
import dk.trustworks.timemanager.client.model.Client;
import dk.trustworks.timemanager.client.model.Project;
import dk.trustworks.timemanager.client.model.TaskWorkerConstraint;
import dk.trustworks.timemanager.time.dto.TableCalenderDTO;
import dk.trustworks.timemanager.time.model.Work;
import dk.trustworks.timemanager.time.repos.WeekRepository;
import dk.trustworks.timemanager.time.repos.WorkRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.Calendar.*;

@Log4j
@RestController
@RequestMapping("/api/timemanager/command")
public class TimeService {

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
	
	@Autowired
	private BudgetCalculator budgetCalculator;
	
    @RequestMapping(value = "/getTaskWeekView")
    @Transactional(readOnly=true)
    //@Cacheable("weeks")
    public TaskWeekView[] getTaskWeekView(@RequestParam("weekNumber") int weekNumber, @RequestParam("year") int year, @RequestParam("userUUID") String userUUID) {
        ArrayList<TaskWeekView> taskWeekViews = new ArrayList<>();
        List<Week> weeks = weekRepository.findByWeekNumberAndYearAndUserUUIDOrderBySortingAsc(weekNumber, year, userUUID);

        for (Week week : weeks) {
        	Task task = taskRepository.findOne(week.getTaskUUID());
            boolean dublet = false;
            for (TaskWeekView tempView : taskWeekViews) {
				if(tempView.getTaskUUID().equals(week.getTaskUUID())) dublet = true; 
			}
            if(dublet) continue;
            TaskWeekView taskWeekView = new TaskWeekView();
            Project project = projectRepository.findOne(task.getProjectUUID());
			Client client = clientRepository.findOne(project.getClientUUID());
			taskWeekView.setTaskName(task.getName() + " / " + project.getName() + " / " + client.getName());
            taskWeekView.setTaskUUID(week.getTaskUUID());
            taskWeekView.setBudgetLeft(getRemainingBudget("TaskWorker", week.getTaskUUID(), userUUID).getRemainingBudget());

            Calendar c = getInstance();
            c.setFirstDayOfWeek(MONDAY);
            c.clear();
            c.set(Calendar.WEEK_OF_YEAR, weekNumber);
            c.set(Calendar.YEAR, year);

            for(int i = 0; i <7; i++) {
                for (Work work : workRepository.findByTaskUUID(week.getTaskUUID())) {
                    if(work.getDay() == c.get(DAY_OF_MONTH) && work.getMonth() == c.get(MONTH) && work.getYear() == c.get(YEAR) && work.getUserUUID().equals(userUUID)) {
                        taskWeekView.addWorkToDay(i, work.getWorkDuration());
                    }
                }
                c.add(DATE, 1);
            }
            taskWeekViews.add(taskWeekView);
        }

        return taskWeekViews.toArray(new TaskWeekView[taskWeekViews.size()]);
    }
    
    @RequestMapping(value = "/remainingBudgetCommand", method = RequestMethod.GET, produces = "application/json")
    @Transactional(readOnly = true)
    public Budget getRemainingBudget(@RequestParam("unitType") String unitType, @RequestParam("unitUUID") String unitUUID, @RequestParam("userUUID") String userUUID) {
        if(unitType.equals("TaskWorker")) {
            return budgetCalculator.calculateRemainingTaskWorkerBudget(unitUUID, userUUID);
        }
        if(unitType.equals("Project")) {
            return budgetCalculator.calculateRemainingProjectBudget(unitUUID);
        }
        if(unitType.equals("ProjectMonthlyBudget")) {
            return budgetCalculator.calculateProjectMonthlyBudget(unitUUID);
        }
        return null;
    }
	
	@RequestMapping(value = "/assignTaskToWeekCommand", method = RequestMethod.POST)
    @Transactional
    //@CacheEvict(value = "weeks", allEntries = true)
    public void assignTaskToWeekView(@RequestBody AssignTaskToWeekCommand assignTaskToWeekCommand) {
		Collection<TaskWorkerConstraint> taskWorkerConstraints = taskWorkerConstraintRepository.findByTaskUUIDAndUserUUID(assignTaskToWeekCommand.getTaskUUID(), assignTaskToWeekCommand.getUserUUID());
		if(taskWorkerConstraints.size() == 0) {
			TaskWorkerConstraint constraint = new TaskWorkerConstraint(UUID.randomUUID().toString(), 0.0, 0.0, assignTaskToWeekCommand.getUserUUID(), assignTaskToWeekCommand.getTaskUUID());
			taskWorkerConstraintRepository.save(constraint);
			taskWorkerConstraints.add(constraint);
		}
        List<Week> weeks = weekRepository.findByWeekNumberAndYearAndUserUUIDAndTaskUUIDOrderBySortingAsc(assignTaskToWeekCommand.getWeekNumber(), assignTaskToWeekCommand.getYear(), assignTaskToWeekCommand.getUserUUID(), assignTaskToWeekCommand.getTaskUUID());

        if(weeks.size() == 0) {
        	Week week = new Week(UUID.randomUUID().toString(), assignTaskToWeekCommand.getWeekNumber(), assignTaskToWeekCommand.getYear(), assignTaskToWeekCommand.getUserUUID(), assignTaskToWeekCommand.getTaskUUID(), 0);
        	weekRepository.save(week);
        }
    }
	
	@RequestMapping(value = "/registerWork", method = RequestMethod.POST)
    @Transactional
    //@CacheEvict(value = {"weeks","works"}, allEntries = true)
    @CacheEvict(value = {"projectYearBudgets","projectYearActual","projectYearUserBudgets","projectYearUserActual","projectYearUserTimeBudgets","projectYearUserTimeActual","budget","projectBudget","taskWorkerBudget","projectMonthlyBudget"}, allEntries=true)
    public void registerWork(@RequestBody RegisterWorkCommand registerWorkCommand) {
		Calendar c = getInstance();
        c.clear();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(WEEK_OF_YEAR, registerWorkCommand.getWeekNumber());
        c.set(YEAR, registerWorkCommand.getYear());
        c.add(DATE, registerWorkCommand.getWeekDay());

        for (Work work : workRepository.findByTaskUUID(registerWorkCommand.getTaskUUID())) {
            if(!work.getUserUUID().equals(registerWorkCommand.getUserUUID())) continue;
            if(work.getDay() == c.get(DAY_OF_MONTH) && work.getMonth() == c.get(MONTH) && work.getYear() == c.get(YEAR)) {
                work.setWorkDuration(registerWorkCommand.getDuration());
                workRepository.save(work);
                return;
            }
        }
        workRepository.save(new Work(UUID.randomUUID().toString(), c.get(DAY_OF_MONTH), c.get(MONTH), c.get(YEAR), registerWorkCommand.getDuration(), registerWorkCommand.getUserUUID(), registerWorkCommand.getTaskUUID()));
    }
	
	@RequestMapping(value = "/cloneTaskToWeek", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void cloneTaskToWeek(@RequestParam("weekNumber") int weekNumber, @RequestParam("year") int year, @RequestParam("userUUID") String userUUID) {
        List<Week> weeks = weekRepository.findByWeekNumberAndYearAndUserUUIDOrderBySortingAsc(weekNumber-1, year, userUUID);
        for (Week week : weeks) {
			weekRepository.save(new Week(UUID.randomUUID().toString(), weekNumber, year, userUUID, week.getTaskUUID(), 0));
		}
    }
	
	@RequestMapping(value = "/getVacationSick")
    @Transactional(readOnly=true)
    public TableCalenderDTO[] getVacationSick(@RequestParam("year") int year, @RequestParam("userUUID") String userUUID) {
		TableCalenderDTO sickDaysDTO = new TableCalenderDTO();
		String sickTaskUUID = "02bf71c5-f588-46cf-9695-5864020eb1c4";
		
		double[] sickHours = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		
		List<Work> works = workRepository.findByYearAndTaskUUIDAndUserUUID(year, sickTaskUUID, userUUID);
		for(int month=0; month<12; month++) {
			for (Work work : works) {
				if(work.getMonth() == month) {
					sickHours[month] += work.getWorkDuration();
				}
			}
		}
		
		sickDaysDTO.setDescription(year+"");
		sickDaysDTO.setAmount(sickHours);
		sickDaysDTO.setUnitUUID("02bf71c5-f588-46cf-9695-5864020eb1c4");
		
		TableCalenderDTO vacDaysDTO = new TableCalenderDTO();
		String vacTaskUUID = "f585f46f-19c1-4a3a-9ebd-1a4f21007282";
		
		double[] vacHours = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		
		works = workRepository.findByYearAndTaskUUIDAndUserUUID(year, vacTaskUUID, userUUID);
		for(int month=0; month<12; month++) {
			for (Work work : works) {
				if(work.getMonth() == month) {
					vacHours[month] += work.getWorkDuration();
				}
			}
		}
		
		vacDaysDTO.setDescription(year+"");
		vacDaysDTO.setAmount(vacHours);
		vacDaysDTO.setUnitUUID("f585f46f-19c1-4a3a-9ebd-1a4f21007282");
		
		return new TableCalenderDTO[] {sickDaysDTO, vacDaysDTO};
	}
}
