package dk.trustworks.timemanager.client.commands;


public class RegisterWorkCommand {

    private String taskUUID;

    private String userUUID;

    private int weekDay;

    private int weekNumber;

    private int year;

    private double duration;

    public RegisterWorkCommand() {
    }

    public RegisterWorkCommand(String taskUUID, String userUUID, int weekDay, int weekNumber, int year, double duration) {
        this.setTaskUUID(taskUUID);
        this.setUserUUID(userUUID);
        this.setWeekDay(weekDay);
        this.setWeekNumber(weekNumber);
        this.setYear(year);
        this.setDuration(duration);
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

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
