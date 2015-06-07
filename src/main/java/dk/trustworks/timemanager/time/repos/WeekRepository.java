package dk.trustworks.timemanager.time.repos;

import dk.trustworks.timemanager.time.model.Week;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "weeks", exported = true)
public interface WeekRepository extends CrudRepository<Week, String> {

	List<Week> findByWeekNumberAndYearAndUserUUIDAndTaskUUIDOrderBySortingAsc(@Param("weekNumber") int weekNumber, @Param("year") int year, @Param("userUUID") String userUUID, @Param("taskUUID") String taskUUID);
	
	List<Week> findByWeekNumberAndYearAndUserUUIDOrderBySortingAsc(@Param("weekNumber") int weekNumber, @Param("year") int year, @Param("userUUID") String userUUID);
	
}