package dk.trustworks.timemanager.time.model;

import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work")
public class Work {

	@Id
	private @NonNull String UUID;
	
	private int day;

	private int month;

	private int year;
	
	@Column(name="workduration")
	private double workDuration;
	
	@Column(name="useruuid")
	private @NonNull String userUUID;
	
	@Column(name="taskuuid")
	private @NonNull String taskUUID;

    public Work() {
    }

    public Work(String UUID, int day, int month, int year, double workDuration, String userUUID, String taskUUID) {
        this.UUID = UUID;
        this.day = day;
        this.month = month;
        this.year = year;
        this.workDuration = workDuration;
        this.userUUID = userUUID;
        this.taskUUID = taskUUID;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(double workDuration) {
        this.workDuration = workDuration;
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
}
