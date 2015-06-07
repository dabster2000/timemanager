package dk.trustworks.timemanager.client.model;

import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="project")
public class Project {
	
	@Id
	private @NonNull String UUID;
	
	@Column(name="customerreference")
	private @NonNull String customerReference;
	
	private @NonNull String name;
	
	@Column(name="commonprice")
	private double commonPrice;
	
	private double budget;
	
	private boolean active = true;
	
	@Column(name="clientuuid")
	private @NonNull String clientUUID;
	
	@Column(name="clientdatauuid")
	private @NonNull String clientDataUUID;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getClientUUID() {
        return clientUUID;
    }

    public void setClientUUID(String clientUUID) {
        this.clientUUID = clientUUID;
    }

    public String getClientDataUUID() {
        return clientDataUUID;
    }

    public void setClientDataUUID(String clientDataUUID) {
        this.clientDataUUID = clientDataUUID;
    }
}
