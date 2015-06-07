package dk.trustworks.timemanager.client.repos;

import dk.trustworks.timemanager.client.model.MonthBudgetHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "monthBudgetsHistory", exported = true)
public interface MonthBudgetHistoryRepository extends CrudRepository<MonthBudgetHistory, String> {
	
	List<MonthBudgetHistory> findByProjectUUID(@Param("projectUUID") String projectUUID);

	List<MonthBudgetHistory> findByProjectUUIDAndYearOrderByMonthAsc(@Param("projectUUID") String projectUUID, @Param("year") int year);
	
	List<MonthBudgetHistory> findByProjectUUIDAndYearAndMonth(@Param("projectUUID") String projectUUID, @Param("year") int year, @Param("month") int month);
	
}
