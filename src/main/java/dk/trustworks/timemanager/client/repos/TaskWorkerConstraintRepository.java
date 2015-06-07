package dk.trustworks.timemanager.client.repos;

import dk.trustworks.timemanager.client.model.TaskWorkerConstraint;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "taskWorkerConstraints", exported = true)
public interface TaskWorkerConstraintRepository extends CrudRepository<TaskWorkerConstraint, String> {

	@Cacheable("taskWorkerConstraints")
	List<TaskWorkerConstraint> findByTaskUUID(@Param("taskUUID") String taskUUID);
	
	@Cacheable("taskWorkerConstraints")
	List<TaskWorkerConstraint> findByTaskUUIDAndUserUUID(@Param("taskUUID") String taskUUID, @Param("userUUID") String userUUID);
	
}
