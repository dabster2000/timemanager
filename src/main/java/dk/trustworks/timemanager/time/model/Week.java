package dk.trustworks.timemanager.time.model;

import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="week")
public class Week {

    @Id
    private @NonNull String UUID;

    @Column(name="weeknumber")
    private int weekNumber;

    private int year;

    @Column(name="useruuid")
    private String userUUID;

    @Column(name="taskuuid")
    private String taskUUID;
    
    @Column(name="sorting")
    private int sorting;

    public Week() {
    }

    public Week(String UUID, int weekNumber, int year, String userUUID, String taskUUID, int sorting) {
        this.UUID = UUID;
        this.weekNumber = weekNumber;
        this.year = year;
        this.userUUID = userUUID;
        this.taskUUID = taskUUID;
        this.sorting = sorting;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getTaskUUID() {
        return taskUUID;
    }

    public void setTaskUUID(String taskUUID) {
        this.taskUUID = taskUUID;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }
}