package com.bits.af.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "book")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookingID")
	private int bookingId;

	@Column(name = "clientID")
	private int clientId;

	@Column(name = "propertyID")
	private int propertyId;

	@Column(name = "Bk_Status")
	private char bookingStatus;

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public char getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(char bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
}
