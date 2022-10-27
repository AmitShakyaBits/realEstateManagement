package com.bits.af.pojo;

import lombok.Data;
import lombok.ToString;

@ToString
public class BookingRequest {

	private int bookingId;
	private int clientId;
	private int propertyId;
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
