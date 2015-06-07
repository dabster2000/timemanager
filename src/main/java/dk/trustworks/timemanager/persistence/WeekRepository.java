package dk.trustworks.timemanager.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import dk.trustworks.framework.persistence.GenericRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hans on 17/03/15.
 */
public class WeekRepository extends GenericRepository {

    public WeekRepository() {
        super();
    }

    public List<Map<String, Object>> findByWeekNumberAndYearAndUserUUIDAndTaskUUIDOrderBySortingAsc(int weekNumber, int year, String userUUID, String taskUUID) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM week w WHERE w.weeknumber = ? AND w.year = ? AND w.useruuid LIKE ? AND taskuuid LIKE ? ORDER BY sorting ASC", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, weekNumber);
            stmt.setInt(2, year);
            stmt.setString(3, userUUID);
            stmt.setString(4, taskUUID);
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String, Object>> findByWeekNumberAndYearAndUserUUIDOrderBySortingAsc(int weekNumber, int year, String userUUID) {
        System.out.println(weekNumber+", "+year+", "+userUUID);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM week w WHERE w.weeknumber = ? AND w.year = ? AND w.useruuid LIKE ? ORDER BY sorting ASC", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, weekNumber);
            stmt.setInt(2, year);
            stmt.setString(3, userUUID);
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void create(JsonNode jsonNode) throws SQLException {
        //testForNull(jsonNode, new String[]{"useruuid", "taskUUID", "weekNumber", "year"});
        System.out.println("Create week: "+jsonNode);
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO week (uuid, taskuuid, useruuid, weeknumber, year) VALUES (?, ?, ?, ?, ?)", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setString(1, UUID.randomUUID().toString());
        stmt.setString(2, jsonNode.get("taskuuid").asText());
        stmt.setString(3, jsonNode.get("useruuid").asText());
        stmt.setInt(4, jsonNode.get("weeknumber").asInt());
        stmt.setInt(5, jsonNode.get("year").asInt());
        stmt.executeUpdate();
        stmt.close();
    }

    @Override
    public void update(JsonNode jsonNode, String uuid) throws SQLException {

    }
}
