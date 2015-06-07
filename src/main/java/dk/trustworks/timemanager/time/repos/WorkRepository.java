package dk.trustworks.timemanager.time.repos;

import dk.trustworks.timemanager.time.model.Work;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "works", exported = true)
public interface WorkRepository extends CrudRepository<Work, String> {

	List<Work> findByTaskUUID(@Param("taskUUID") String taskUUID);
	
	List<Work> findByYear(@Param("year") int year);
	
	List<Work> findByYearAndUserUUID(@Param("year") int year, @Param("userUUID") String userUUID);
	
	List<Work> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
	
	List<Work> findByYearAndMonthAndTaskUUIDAndUserUUID(@Param("year") int year, @Param("month") int month, @Param("taskUUID") String taskUUID, @Param("userUUID") String userUUID);
	
	List<Work> findByYearAndMonthAndDayAndTaskUUIDAndUserUUID(@Param("year") int year, @Param("month") int month, @Param("day") int day, @Param("taskUUID") String taskUUID, @Param("userUUID") String userUUID);
	
	List<Work> findByYearAndMonthAndTaskUUID(@Param("year") int year, @Param("month") int month, @Param("taskUUID") String taskUUID);
	
	List<Work> findByYearAndTaskUUIDAndUserUUID(@Param("year") int year, @Param("taskUUID") String taskUUID, @Param("userUUID") String userUUID);
	
}
