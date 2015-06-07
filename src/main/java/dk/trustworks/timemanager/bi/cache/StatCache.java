package dk.trustworks.timemanager.bi.cache;

import dk.trustworks.timemanager.bi.model.ProjectYearEconomy;
import dk.trustworks.timemanager.user.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Component
public class StatCache {

	private UUID uuid = UUID.randomUUID();
	
	private List<ProjectYearEconomy> projectYearBudgets = new ArrayList<>();
	private List<ProjectYearEconomy> projectYearActual = new ArrayList<>();
	private Map<User, List<ProjectYearEconomy>> userProjectYearBudgets = new HashMap<>();
	private Map<User, List<ProjectYearEconomy>> userProjectYearActual = new HashMap<>();
	private Hashtable<String, User> users = new Hashtable<>();
	
}
