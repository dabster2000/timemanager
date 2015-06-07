package dk.trustworks.timemanager.client.repos;

import dk.trustworks.timemanager.client.model.Project;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "projects", exported = true)
public interface ProjectRepository extends CrudRepository<Project, String> {
	
	List<Project> findByOrderByNameAsc();
	
	List<Project> findByActiveTrueOrderByNameAsc();
	
	List<Project> findByClientUUID(@Param("clientUUID") String clientUUID);
	
	@Cacheable("projects")
	List<Project> findByClientUUIDAndActiveTrue(@Param("clientUUID") String clientUUID);
	
	List<Project> findByClientUUIDOrderByNameAsc(@Param("clientUUID") String clientUUID);
	
	@Cacheable("projects")
	List<Project> findByClientUUIDAndActiveTrueOrderByNameAsc(@Param("clientUUID") String clientUUID);
	
}
