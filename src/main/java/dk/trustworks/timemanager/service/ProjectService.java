package dk.trustworks.timemanager.service;

import com.fasterxml.jackson.databind.JsonNode;
import dk.trustworks.framework.network.RemoteEntity;
import dk.trustworks.framework.service.DefaultRemoteService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by hans on 24/04/15.
 */
public class ProjectService extends DefaultRemoteService {

    private final RemoteEntity remoteEntity = new RemoteEntity();

    @Override
    public List<Map<String, Object>> getAllEntities(String entityName) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Map<String, Object> getOneEntity(String entityName, String uuid) {
        return remoteEntity.getOneClientEntity(entityName, uuid);
    }

    @Override
    public void create(JsonNode clientJsonNode) throws SQLException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void update(JsonNode clientJsonNode, String uuid) throws SQLException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String getResourcePath() {
        return "projects";
    }
}
