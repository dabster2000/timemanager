package dk.trustworks.timemanager.client.repos;

import dk.trustworks.timemanager.client.model.Contract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "contracts", exported = true)
public interface ContractRepository extends CrudRepository<Contract, String> {

}
