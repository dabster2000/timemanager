package dk.trustworks.timemanager.client.model;

import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="clientdata")
public class ClientData {

	@Id
	private @NonNull String UUID;
	
	@Column(name="clientuuid")
	private @NonNull String clientUUID;
	
	@Column(name="clientname")
	private String clientName;
	
	@Column(name="streetnamenumber")
	private @NonNull String streetNameNumber;
	
	private @NonNull String city;
	
	@Column(name="postalcode")
	private int postalCode;
	
	@Column(name="otheraddressinfo")
	private @NonNull String otherAddressInfo;
	
	@Column(name="ean")
	private @NonNull String EAN;
	
	@Column(name="contactperson")
	private @NonNull String contactPerson;
	
	private @NonNull String cvr;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getClientUUID() {
        return clientUUID;
    }

    public void setClientUUID(String clientUUID) {
        this.clientUUID = clientUUID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getStreetNameNumber() {
        return streetNameNumber;
    }

    public void setStreetNameNumber(String streetNameNumber) {
        this.streetNameNumber = streetNameNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getOtherAddressInfo() {
        return otherAddressInfo;
    }

    public void setOtherAddressInfo(String otherAddressInfo) {
        this.otherAddressInfo = otherAddressInfo;
    }

    public String getEAN() {
        return EAN;
    }

    public void setEAN(String EAN) {
        this.EAN = EAN;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getCvr() {
        return cvr;
    }

    public void setCvr(String cvr) {
        this.cvr = cvr;
    }
}
