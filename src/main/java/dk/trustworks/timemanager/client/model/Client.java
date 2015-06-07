package dk.trustworks.timemanager.client.model;

import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="client")
public class Client {
	
	@Id
	private @NonNull String UUID;
	
    private @NonNull String name;
    
    @Column(name="contactname")
	private @NonNull String contactName;
    
    @Column(name="commonprice")
	private double commonPrice;

    private boolean active;

    public Client() {
    }

    public Client(String UUID, String name, String contactName, double commonPrice, boolean active) {
        this.UUID = UUID;
        this.name = name;
        this.contactName = contactName;
        this.commonPrice = commonPrice;
        this.active = active;
    }

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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public double getCommonPrice() {
        return commonPrice;
    }

    public void setCommonPrice(double commonPrice) {
        this.commonPrice = commonPrice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
