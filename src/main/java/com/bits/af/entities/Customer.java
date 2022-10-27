package com.bits.af.entities;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "clientID")
	private int clientId;
	private String clientName;
	private String clientContactNumber;
	private String clientAddress;
	private String clientCity;
	private String clientEmailAddress;
	private String clientUniqueId;
	private String clientPassword;
	
	public String getClientName() {
		return clientName;
	}

	public String getClientUniqueId() {
		return clientUniqueId;
	}

	public void setClientUniqueId(String clientUniqueId) {
		this.clientUniqueId = clientUniqueId;
	}

	public String getClientCity() {
		return clientCity;
	}

	public void setClientCity(String clientCity) {
		this.clientCity = clientCity;
	}

	public String getClientEmailAddress() {
		return clientEmailAddress;
	}

	public void setClientEmailAddress(String clientEmailAddress) {
		this.clientEmailAddress = clientEmailAddress;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientContactNumber() {
		return clientContactNumber;
	}

	public void setClientContactNumber(String clientContactNumber) {
		this.clientContactNumber = clientContactNumber;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getClientPassword() {
		return clientPassword;
	}

	public void setClientPassword(String clientPassword) {
		this.clientPassword = clientPassword;
	}
	
	
	

}
