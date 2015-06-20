package dk.trustworks.timemanager.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import dk.trustworks.framework.persistence.GenericRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hans on 17/03/15.
 */
public class WorkRepository extends GenericRepository {

    private static final Logger log = LogManager.getLogger(WorkRepository.class);

    public WorkRepository() {
        super();
    }

    public List<Map<String, Object>> findByTaskUUID(String taskUUID) {
        log.debug("WorkRepository.findByTaskUUID");
        log.debug("taskuuid = [" + taskUUID + "]");
        try (org.sql2o.Connection con = database.open()) {
            return getEntitiesFromMapSet(con.createQuery("select yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid" +
                    "from work yt inner join( " +
                    "select uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "from work WHERE taskuuid LIKE :taskuuid " +
                    "group by day, month, year " +
                    ") ss on yt.month = ss.month and yt.year = ss.year and yt.day = ss.day and yt.created = ss.created and yt.taskuuid = ss.taskuuid and yt.useruuid = ss.useruuid;")
                    .addParameter("taskuuid", taskUUID)
                    .executeAndFetchTable().asList());
        } catch (Exception e) {
            log.error("LOG00610:", e);
        }
        return new ArrayList<>();
        /*
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
        */
    }

    public List<Map<String, Object>> findByYear(String year) {
        log.debug("WorkRepository.findByYear");
        log.debug("year = [" + year + "]");
        try (org.sql2o.Connection con = database.open()) {
            return getEntitiesFromMapSet(con.createQuery("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE year = :year " +
                    "GROUP BY year) ss " +
                    "ON yt.year = ss.year AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;")
                    .addParameter("year", year)
                    .executeAndFetchTable().asList());
        } catch (Exception e) {
            log.error("LOG00620:", e);
        }
        return new ArrayList<>();
        /*
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
        */
    }

    public List<Map<String, Object>> findByYearAndUserUUID(String year, String userUUID) {
        log.debug("WorkRepository.findByYearAndUserUUID");
        log.debug("year = [" + year + "], userUUID = [" + userUUID + "]");
        try (org.sql2o.Connection con = database.open()) {
            return getEntitiesFromMapSet(con.createQuery("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE useruuid LIKE :useruuid AND year = :year" +
                    "GROUP BY day, month, year) ss " +
                    "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;")
                    .addParameter("useruuid", userUUID)
                    .addParameter("year", year)
                    .executeAndFetchTable().asList());
        } catch (Exception e) {
            log.error("LOG00630:", e);
        }
        return new ArrayList<>();
        /*
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
        */
    }

    public List<Map<String, Object>> findByYearAndMonth(String year, String month) {
        log.debug("WorkRepository.findByYearAndMonth");
        log.debug("year = [" + year + "], month = [" + month + "]");
        try (org.sql2o.Connection con = database.open()) {
            return getEntitiesFromMapSet(con.createQuery("SELECT yt.uuid, yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE month = :month AND year = :year " +
                    "GROUP BY day, month, year, taskuuid, useruuid) ss " +
                    "ON yt.day = ss.day AND yt.month = ss.month AND yt.year = ss.year AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;")
                    .addParameter("month", month)
                    .addParameter("year", year)
                    .executeAndFetchTable().asList());
        } catch (Exception e) {
            log.error("LOG00640:", e);
        }
        return new ArrayList<>();
        /*
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT yt.uuid, yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                            "FROM work yt INNER JOIN( " +
                            "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                            "FROM work WHERE month = ? AND year = ? " +
                            "GROUP BY day, month, year, taskuuid, useruuid) ss " +
                            "ON yt.day = ss.day AND yt.month = ss.month AND yt.year = ss.year AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;",
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
        */
    }

    public List<Map<String, Object>> findByYearAndMonthAndTaskUUIDAndUserUUID(String year, String month, String taskUUID, String userUUID) {
        log.debug("WorkRepository.findByYearAndMonthAndTaskUUIDAndUserUUID");
        log.debug("year = [" + year + "], month = [" + month + "], taskUUID = [" + taskUUID + "], userUUID = [" + userUUID + "]");
        try (org.sql2o.Connection con = database.open()) {
            return getEntitiesFromMapSet(con.createQuery("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE taskuuid LIKE :taskuuid AND useruuid LIKE useruuid AND month = :month AND year = :year " +
                    "GROUP BY day, month, year) ss " +
                    "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;")
                    .addParameter("taskuuid", taskUUID)
                    .addParameter("useruuid", userUUID)
                    .addParameter("month", month)
                    .addParameter("year", year)
                    .executeAndFetchTable().asList());
        } catch (Exception e) {
            log.error("LOG00650:", e);
        }
        return new ArrayList<>();
        /*
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
        */
    }

    public List<Map<String, Object>> findByYearAndMonthAndDayAndTaskUUIDAndUserUUID(int year, int month, int day, String taskUUID, String userUUID) {
        log.debug("WorkRepository.findByYearAndMonthAndDayAndTaskUUIDAndUserUUID");
        log.debug("year = [" + year + "], month = [" + month + "], day = [" + day + "], taskUUID = [" + taskUUID + "], userUUID = [" + userUUID + "]");
        try (org.sql2o.Connection con = database.open()) {
            return getEntitiesFromMapSet(con.createQuery("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE taskuuid LIKE :taskuuid AND useruuid LIKE :useruuid AND year = :year AND month = :month AND day = :day " +
                    "GROUP BY day, month, year) ss " +
                    "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;")
                    .addParameter("taskuuid", taskUUID)
                    .addParameter("useruuid", userUUID)
                    .addParameter("month", month)
                    .addParameter("year", year)
                    .addParameter("day", day)
                    .executeAndFetchTable().asList());
        } catch (Exception e) {
            log.error("LOG00660:", e);
        }
        return new ArrayList<>();
        /*
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
        */
    }

    public List<Map<String, Object>> findByTaskUUIDAndUserUUID(String taskUUID, String userUUID) {
        log.debug("WorkRepository.findByTaskUUIDAndUserUUID");
        log.debug("taskUUID = [" + taskUUID + "], userUUID = [" + userUUID + "]");
        try (org.sql2o.Connection con = database.open()) {
            return getEntitiesFromMapSet(con.createQuery("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE taskuuid LIKE :taskuuid AND useruuid LIKE :useruuid " +
                    "GROUP BY day, month, year) ss " +
                    "ON yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;")
                    .addParameter("taskuuid", taskUUID)
                    .addParameter("useruuid", userUUID)
                    .executeAndFetchTable().asList());
        } catch (Exception e) {
            log.error("LOG00670:", e);
        }
        return new ArrayList<>();
        /*
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
        */
    }

    public List<Map<String, Object>> findByYearAndMonthAndTaskUUID(int year, int month, String taskUUID) {
        log.debug("WorkRepository.findByYearAndMonthAndTaskUUID");
        log.debug("year = [" + year + "], month = [" + month + "], taskUUID = [" + taskUUID + "]");
        try (org.sql2o.Connection con = database.open()) {
            return getEntitiesFromMapSet(con.createQuery("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE taskuuid LIKE :taskuuid AND month = :month AND year = :year " +
                    "GROUP BY day, month, year) ss " +
                    "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;")
                    .addParameter("taskuuid", taskUUID)
                    .addParameter("month", month)
                    .addParameter("year", year)
                    .executeAndFetchTable().asList());
        } catch (Exception e) {
            log.error("LOG00680:", e);
        }
        return new ArrayList<>();
        /*
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
        */
    }

    public List<Map<String, Object>> findByYearAndTaskUUIDAndUserUUID(int year, String taskUUID, String userUUID) {
        log.debug("WorkRepository.findByYearAndTaskUUIDAndUserUUID");
        log.debug("year = [" + year + "], taskUUID = [" + taskUUID + "], userUUID = [" + userUUID + "]");
        try (org.sql2o.Connection con = database.open()) {
            return getEntitiesFromMapSet(con.createQuery("SELECT yt.month, yt.year, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, month, year, day, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE taskuuid LIKE :taskuuid AND useruuid LIKE :useruuid AND year = :year " +
                    "GROUP BY day, month, year) ss " +
                    "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid;")
                    .addParameter("taskuuid", taskUUID)
                    .addParameter("useruuid", userUUID)
                    .addParameter("year", year)
                    .executeAndFetchTable().asList());
        } catch (Exception e) {
            log.error("LOG00690:", e);
        }
        return new ArrayList<>();
        /*
        return new ArrayList<>();
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
        */
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
        log.debug("WorkRepository.calculateTaskUserTotalDuration");
        log.debug("taskUUID = [" + taskUUID + "], userUUID = [" + userUUID + "]");
        try (org.sql2o.Connection con = database.open()) {
            return con.createQuery("SELECT sum(workduration) sum FROM ( " +
                    "SELECT yt.year, yt.month, yt.day, yt.created, yt.workduration, yt.taskuuid, yt.useruuid " +
                    "FROM work yt INNER JOIN( " +
                    "SELECT uuid, day, month, year, workduration, taskuuid, useruuid, max(created) created " +
                    "FROM work WHERE taskuuid LIKE :taskuuid AND useruuid LIKE :useruuid " +
                    "GROUP BY day, month, year) ss " +
                    "ON yt.month = ss.month AND yt.year = ss.year AND yt.day = ss.day AND yt.created = ss.created " +
                    "AND yt.taskuuid = ss.taskuuid AND yt.useruuid = ss.useruuid " +
                    ") we;")
                    .addParameter("taskuuid", taskUUID)
                    .addParameter("useruuid", userUUID)
                    .executeScalar(Double.class);
        } catch (Exception e) {
            log.error("LOG00700:", e);
        }
        return 0.0;
        /*
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
        */
    }

    @Override
    public void create(JsonNode jsonNode) throws SQLException {
        log.debug("WorkRepository.create");
        log.debug("jsonNode = [" + jsonNode + "]");
        try (org.sql2o.Connection con = database.open()) {
            con.createQuery("INSERT INTO work (uuid, day, month, year, taskuuid, useruuid, workduration, created)" +
                    " VALUES (:uuid, :day, :month, :year, :taskuuid, :useruuid, :workduration, :created)")
                    .addParameter("uuid", jsonNode.get("uuid").asText(UUID.randomUUID().toString()))
                    .addParameter("day", jsonNode.get("day").asInt())
                    .addParameter("month", jsonNode.get("month").asInt())
                    .addParameter("year", jsonNode.get("year").asInt())
                    .addParameter("taskuuid", jsonNode.get("taskuuid").asText())
                    .addParameter("useruuid", jsonNode.get("useruuid").asText())
                    .addParameter("workduration", jsonNode.get("workduration").asInt())
                    .addParameter("created", Timestamp.from(Instant.now()))
                    .executeUpdate();
        } catch (Exception e) {
            log.error("LOG00710:", e);
        }
        /*
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
        */
    }

    @Override
    public void update(JsonNode jsonNode, String uuid) throws SQLException {

    }
}
