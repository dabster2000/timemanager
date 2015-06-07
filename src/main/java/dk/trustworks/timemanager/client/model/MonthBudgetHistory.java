package dk.trustworks.timemanager.client.model;

import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="monthbudgethistory")
public class MonthBudgetHistory {

	@Id
    private @NonNull String UUID;

    private int year;

    private int month;

    private double budget;
    
    @Column(name="projectuuid")
    private @NonNull String projectUUID;
    
    public MonthBudgetHistory(MonthBudget monthBudget) {
    	this.setYear(monthBudget.getYear());
    	this.setMonth(monthBudget.getMonth());
    	this.setBudget(monthBudget.getBudget());
    	this.setProjectUUID(monthBudget.getProjectUUID());
    	this.setUUID(java.util.UUID.randomUUID().toString());
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getProjectUUID() {
        return projectUUID;
    }

    public void setProjectUUID(String projectUUID) {
        this.projectUUID = projectUUID;
    }
}
