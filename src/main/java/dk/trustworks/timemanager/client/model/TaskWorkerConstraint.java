package dk.trustworks.timemanager.client.model;


import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="taskworkerconstraint")
public class TaskWorkerConstraint {

	@Id
    private @NonNull String UUID;

    private double price;

    private double budget;

    @Column(name="useruuid")
	private @NonNull String userUUID;
    
    @Column(name="taskuuid")
	private @NonNull String taskUUID;

    public TaskWorkerConstraint() {
    }

    public TaskWorkerConstraint(String UUID, double price, double budget, String userUUID, String taskUUID) {
        this.UUID = UUID;
        this.price = price;
        this.budget = budget;
        this.userUUID = userUUID;
        this.taskUUID = taskUUID;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
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
