package dk.trustworks.timemanager.client.commands;

public class AssignTaskToWeekCommand {

    private int weekNumber;

    private int year;

    private String taskUUID;

    private String userUUID;

    public AssignTaskToWeekCommand() {
    }

    public AssignTaskToWeekCommand(int weekNumber, int year, String taskUUID, String userUUID) {
        this.weekNumber = weekNumber;
        this.year = year;
        this.taskUUID = taskUUID;
        this.userUUID = userUUID;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTaskUUID() {
        return taskUUID;
    }

    public void setTaskUUID(String taskUUID) {
        this.taskUUID = taskUUID;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }
}
