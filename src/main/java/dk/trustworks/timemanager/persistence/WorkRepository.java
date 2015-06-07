package dk.trustworks.timemanager.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import dk.trustworks.framework.persistence.GenericRepository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hans on 17/03/15.
 */
public class WorkRepository extends GenericRepository {

    public WorkRepository() {
        super();
    }

    public List<Map<String, Object>> findByTaskUUID(String taskuuid) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("select yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "from work yt inner join( " +
                    "select uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "from work WHERE taskuuid LIKE ? " +
                    "group by day, month, year " +
                    ") ss on yt.month = ss.month and yt.year = ss.year and yt.day = ss.day and yt.created = ss.created and yt.taskuuid = ss.taskuuid and yt.useruuid = ss.useruuid;", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, taskuuid);
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String, Object>> findByYear(String year) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE year = ? " +
                    "GROUP BY year) ss " +
                    "ON yt.year = ss.year AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, Integer.parseInt(year));
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String, Object>> findByYearAndUserUUID(String year, String userUUID) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                            "FROM work yt INNER JOIN( " +
                            "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                            "FROM work WHERE useruuid LIKE ? AND year = ?" +
                            "GROUP BY day, month, year) ss " +
                            "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userUUID);
            stmt.setInt(2, Integer.parseInt(year));
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            resultSet.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String, Object>> findByYearAndMonth(String year, String month) {
        System.out.println("WorkRepository.findByYearAndMonth");
        System.out.println("year = [" + year + "], month = [" + month + "]");
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                            "FROM work yt INNER JOIN( " +
                            "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                            "FROM work WHERE month = ? AND year = ? " +
                            "GROUP BY day, month, year, taskuuid, useruuid) ss " +
                            "ON yt.month = ss.month AND yt.year = ss.year AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, Integer.parseInt(month));
            stmt.setInt(2, Integer.parseInt(year));
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            System.out.println("result = " + result);
            resultSet.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String, Object>> findByYearAndMonthAndTaskUUIDAndUserUUID(String year, String month, String taskUUID, String userUUID) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                            "FROM work yt INNER JOIN( " +
                            "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                            "FROM work WHERE taskuuid LIKE ? AND useruuid LIKE ? AND month = ? AND year = ? " +
                            "GROUP BY day, month, year) ss " +
                            "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, taskUUID);
            stmt.setString(2, userUUID);
            stmt.setInt(3, Integer.parseInt(month));
            stmt.setInt(4, Integer.parseInt(year));
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            resultSet.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String, Object>> findByYearAndMonthAndDayAndTaskUUIDAndUserUUID(int year, int month, int day, String taskUUID, String userUUID) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE taskuuid LIKE ? AND useruuid LIKE ? AND year = ? AND month = ? AND day = ? " +
                    "GROUP BY day, month, year) ss " +
                    "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, taskUUID);
            stmt.setString(2, userUUID);
            stmt.setInt(3, year);
            stmt.setInt(4, month);
            stmt.setInt(5, day);
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String, Object>> findByTaskUUIDAndUserUUID(String taskUUID, String userUUID) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                            "FROM work yt INNER JOIN( " +
                            "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                            "FROM work WHERE taskuuid LIKE ? AND useruuid LIKE ? " +
                            "GROUP BY day, month, year) ss " +
                            "ON yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, taskUUID);
            stmt.setString(2, userUUID);
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String, Object>> findByYearAndMonthAndTaskUUID(int year, int month, String taskUUID) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE taskuuid LIKE ? AND month = ? AND year = ? " +
                    "GROUP BY day, month, year) ss " +
                    "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, taskUUID);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map<String, Object>> findByYearAndTaskUUIDAndUserUUID(int year, String taskUUID, String userUUID) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE taskuuid LIKE ? AND useruuid LIKE ? AND year = ? " +
                    "GROUP BY day, month, year) ss " +
                    "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, taskUUID);
            stmt.setString(2, userUUID);
            stmt.setInt(3, year);
            ResultSet resultSet = stmt.executeQuery();
            result = getEntitiesFromResultSet(resultSet);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    select sum(workduration) sum from (
    select yt.year, yt.month, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid
    from work yt inner join(
    select uuid, day, month, year, workduration, taskuuid, useruuid, max(created) created
    from work WHERE taskuuid LIKE '217e5c68-eb72-40b6-a3d2-099c34f89405' and useruuid LIKE '7948c5e8-162c-4053-b905-0f59a21d7746'
    group by month, year) ss
    on yt.month = ss.month and yt.year = ss.year and yt.day = ss.day and yt.created = ss.created and yt.taskuuid = ss.taskuuid and yt.useruuid = ss.useruuid
    ) we;
     */

    public double calculateTaskUserTotalDuration(String taskUUID, String userUUID) {
        double result = 0.0;
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT sum(workduration) sum FROM ( " +
                            "SELECT yt.year, yt.month, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                            "FROM work yt INNER JOIN( " +
                            "SELECT uuid, day, month, year, workduration, taskuuid, useruuid, max(created) created " +
                            "FROM work WHERE taskuuid LIKE ? AND useruuid LIKE ? " +
                            "GROUP BY day, month, year) ss " +
                            "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created " +
                            "AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid " +
                            ") we;",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, taskUUID);
            stmt.setString(2, userUUID);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            result = resultSet.getDouble("sum");
            resultSet.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void create(JsonNode jsonNode) throws SQLException {
        System.out.println("WorkRepository.create");
        System.out.println("jsonNode = [" + jsonNode + "]");
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO work (uuid, day, month, year, taskuuid, useruuid, workduration, created) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setString(1, UUID.randomUUID().toString());
        stmt.setInt(2, jsonNode.get("day").asInt());
        stmt.setInt(3, jsonNode.get("month").asInt());
        stmt.setInt(4, jsonNode.get("year").asInt());
        stmt.setString(5, jsonNode.get("taskuuid").asText());
        stmt.setString(6, jsonNode.get("useruuid").asText());
        stmt.setInt(7, jsonNode.get("workduration").asInt());
        stmt.setTimestamp(8, Timestamp.from(Instant.now()));
        stmt.executeUpdate();
        stmt.close();
        connection.close();
        System.out.println("saved!");
    }

    @Override
    public void update(JsonNode jsonNode, String uuid) throws SQLException {

    }
}
