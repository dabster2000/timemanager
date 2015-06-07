package dk.trustworks.timemanager.client.repos;

import dk.trustworks.timemanager.client.model.ClientData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "clientdatas", exported = true)
public interface ClientDataRepository extends CrudRepository<ClientData, String> {

	//@Query("SELECT * FROM clientdata WHERE clientuuid = ?0")
	List<ClientData> findByClientUUID(@Param("clientUUID") String clientUUID);
	
}