package dk.trustworks.timemanager.client.model;

import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="task")
public class Task {

	@Id
	private @NonNull String UUID;
	
	private @NonNull String name;
	
	@Column(name="commonprice")
	private double commonPrice;
	
	private double budget;
	
	@Column(name="projectuuid")
	private @NonNull String projectUUID;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCommonPrice() {
        return commonPrice;
    }

    public void setCommonPrice(double commonPrice) {
        this.commonPrice = commonPrice;
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
