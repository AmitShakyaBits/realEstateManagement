package com.bits.af.entities;

import java.sql.Date;

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
@Table(name = "transaction")
@ToString
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transactiond")
	private int transactionId;

	@Column(name = "bookingid")
	private int bookingId;

	@Column(name = "clientid")
	private int clientId;

	@Column(name = "propertyid")
	private int propertyId;

	@Column(name = "bank_refid")
	private String bankReferenceId;

	@Column(name = "tr_startdate")
	private Date transactionStartDate;

	@Column(name = "tr_enddate")
	private Date transactionEndDate;

	@Column(name = "tr_status")
	private char transactionStatus;

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

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

	public String getBankReferenceId() {
		return bankReferenceId;
	}

	public void setBankReferenceId(String backReferenceId) {
		this.bankReferenceId = backReferenceId;
	}

	public Date getTransactionStartDate() {
		return transactionStartDate;
	}

	public void setTransactionStartDate(Date transactionStartDate) {
		this.transactionStartDate = transactionStartDate;
	}

	public Date getTransactionEndDate() {
		return transactionEndDate;
	}

	public void setTransactionEndDate(Date transactionEndDate) {
		this.transactionEndDate = transactionEndDate;
	}

	public char getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(char transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

}
