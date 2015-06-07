package dk.trustworks.timemanager.client.dto;

public class TaskWorkerConstraintView {

    private String taskWorkerContraintUUID;

    private String workerUUID;

    private String workerName;

    private double rate;

    private double budget;

    public TaskWorkerConstraintView() {
    }

    public TaskWorkerConstraintView(String taskWorkerContraintUUID, String workerUUID, String workerName, double rate, double budget) {
        this.taskWorkerContraintUUID = taskWorkerContraintUUID;
        this.workerUUID = workerUUID;
        this.workerName = workerName;
        this.rate = rate;
        this.budget = budget;
    }

    public String getTaskWorkerContraintUUID() {
        return taskWorkerContraintUUID;
    }

    public void setTaskWorkerContraintUUID(String taskWorkerContraintUUID) {
        this.taskWorkerContraintUUID = taskWorkerContraintUUID;
    }

    public String getWorkerUUID() {
        return workerUUID;
    }

    public void setWorkerUUID(String workerUUID) {
        this.workerUUID = workerUUID;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
