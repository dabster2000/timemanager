package dk.trustworks.timemanager.client.repos;

import dk.trustworks.timemanager.client.model.Task;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "tasks", exported = true)
public interface TaskRepository extends CrudRepository<Task, String> {

	@Cacheable("tasks")
	List<Task> findByProjectUUID(@Param("projectUUID") String projectUUID);
	
	@Cacheable("tasks")
	List<Task> findByProjectUUIDOrderByNameAsc(@Param("projectUUID") String projectUUID);
	
}
