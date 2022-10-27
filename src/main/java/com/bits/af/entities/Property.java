package com.bits.af.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "property")
public class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "propertyID")
	private Integer propertyId;

	@Column(name = "pty_name")
	private String propertyName;

	@Column(name = "pty_town")
	private String propertyTown;

	@Column(name = "pty_city")
	private String propertyCity;

	@Column(name = "pty_state")
	private String propertyState;

	@Column(name = "pty_category")
	private String propertyCategory;

	@Column(name = "pty_type")
	private String propertyType;

	@Column(name = "pty_price")
	private float propertyPrice;

	@Column(name = "pty_picture")
	private String propertyPicture;

	@Column(name = "pty_status")
	private String propertyStatus;

	@Column(name = "pty_owner")
	private String propertyOwner;

	@Column(name = "pty_unique")
	private String propertyUniqueId;
	
	@Column(name = "pty_area")
	private Integer propertyArea;

	@Column(name = "pty_bed")
	private Integer propertyBed;

	@Column(name = "pty_country")
	private String propertyCountry;

	public String getPropertyCountry() {
		return propertyCountry;
	}

	public void setPropertyCountry(String propertyCountry) {
		this.propertyCountry = propertyCountry;
	}

	@Column(name = "pty_zipcode")
	private Integer propertyZipCode;

	public Integer getPropertyZipCode() {
		return propertyZipCode;
	}

	public void setPropertyZipCode(Integer propertyZipCode) {
		this.propertyZipCode = propertyZipCode;
	}

	@Column(name = "pty_description")
	private String propertyDescription;

	public Date getPropertyAvailableDate() {
		return propertyAvailableDate;
	}

	public void setPropertyAvailableDate(Date propertyAvailableDate) {
		this.propertyAvailableDate = propertyAvailableDate;
	}

	@Column(name = "pty_available_date")
	private Date propertyAvailableDate;

	public String getPropertyDescription() {
		return propertyDescription;
	}

	public void setPropertyDescription(String propertyDescription) {
		this.propertyDescription = propertyDescription;
	}

	public Integer getPropertyArea() {
		return propertyArea;
	}

	public void setPropertyArea(Integer propertyArea) {
		this.propertyArea = propertyArea;
	}

	public Integer getPropertyBed() {
		return propertyBed;
	}

	public void setPropertyBed(Integer propertyBed) {
		this.propertyBed = propertyBed;
	}

	public Integer getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Column(name = "pty_owner_id")
	private Integer propertyOwnerId;

	public Integer getPropertyOwnerId() {
		return propertyOwnerId;
	}

	public void setPropertyOwnerId(Integer propertyOwnerId) {
		this.propertyOwnerId = propertyOwnerId;
	}

	public String getPropertyUniqueId() {
		return propertyUniqueId;
	}

	public void setPropertyUniqueId(String propertyUniqueId) {
		this.propertyUniqueId = propertyUniqueId;
	}

	public String getPropertyOwner() {
		return propertyOwner;
	}

	public void setPropertyOwner(String propertyOwner) {
		this.propertyOwner = propertyOwner;
	}

	public String getPropertyTown() {
		return propertyTown;
	}

	public void setPropertyTown(String propertyTown) {
		this.propertyTown = propertyTown;
	}

	public String getPropertyCity() {
		return propertyCity;
	}

	public void setPropertyCity(String propertyCity) {
		this.propertyCity = propertyCity;
	}

	public String getPropertyState() {
		return propertyState;
	}

	public void setPropertyState(String propertyState) {
		this.propertyState = propertyState;
	}

	public String getPropertyCategory() {
		return propertyCategory;
	}

	public void setPropertyCategory(String propertyCategory) {
		this.propertyCategory = propertyCategory;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public float getPropertyPrice() {
		return propertyPrice;
	}

	public void setPropertyPrice(float propertyPrice) {
		this.propertyPrice = propertyPrice;
	}

	public String getPropertyPicture() {
		return propertyPicture;
	}

	public void setPropertyPicture(String propertyPicture) {
		this.propertyPicture = propertyPicture;
	}

	public String getPropertyStatus() {
		return propertyStatus;
	}

	public void setPropertyStatus(String propertyStatus) {
		this.propertyStatus = propertyStatus;
	}

}
