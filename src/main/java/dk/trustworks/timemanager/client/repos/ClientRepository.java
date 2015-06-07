package dk.trustworks.timemanager.client.repos;

import dk.trustworks.timemanager.client.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "clients", exported = true)
public interface ClientRepository extends CrudRepository<Client, String> {
	
	//@Cacheable("clients")
	List<Client> findByActiveTrue();
	
	//@Cacheable("clients")
	List<Client> findByActiveTrueOrderByNameAsc();
	
}
