package dk.trustworks.timemanager.client.repos;

import dk.trustworks.timemanager.client.model.MonthBudget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "monthBudgets", exported = true)
public interface MonthBudgetRepository extends CrudRepository<MonthBudget, String> {
	
	//@Cacheable("monthBudgets")
	List<MonthBudget> findByProjectUUID(@Param("projectUUID") String projectUUID);
	
	//@Cacheable("monthBudgets")
	List<MonthBudget> findByProjectUUIDAndYear(@Param("projectUUID") String projectUUID, @Param("year") int year);

	//@Cacheable("monthBudgets")
	List<MonthBudget> findByProjectUUIDAndYearOrderByMonthAsc(@Param("projectUUID") String projectUUID, @Param("year") int year);
	
	//@Cacheable("monthBudgets")
	List<MonthBudget> findByProjectUUIDAndYearAndMonth(@Param("projectUUID") String projectUUID, @Param("year") int year, @Param("month") int month);
	
}
