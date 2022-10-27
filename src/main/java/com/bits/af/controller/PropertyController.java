package com.bits.af.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bits.af.entities.Customer;
import com.bits.af.entities.Property;
import com.bits.af.pojo.PropertyRequest;
import com.bits.af.repository.CustomerRepository;
import com.bits.af.repository.PropertyRepository;
import com.bits.af.utils.CommonUtils;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;

@RestController
@RequestMapping("/property")
public class PropertyController {
	@Autowired
	private PropertyRepository propertyRepo;
	
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CustomerRepository customerRepo;

	@GetMapping(produces = "application/json")
	public ResponseEntity<List<Property>> listAll() {
		List<Property> propertyInfo = propertyRepo.findAll();
		return ResponseEntity.ok(propertyInfo);
	}

	@GetMapping(path = "/owner/{propertyOwnerId}", produces = "application/json")
	public ResponseEntity<List<Property>> findByPropertyOwner(@PathVariable Integer propertyOwnerId) {
		List<Property> properties = propertyRepo.findBypropertyOwnerId(propertyOwnerId);
		return new ResponseEntity<>(properties, HttpStatus.OK);
	}

	@GetMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<Property> listById(@PathVariable Integer id) {
		Optional<Property> properties = propertyRepo.findById(id);

		if (properties.isPresent())
			return ResponseEntity.ok(properties.get());
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping(path = "delete/{id}")
	public ResponseEntity deleteProperty(@PathVariable Integer id) throws Exception {
		try {
			Optional<Property> property = propertyRepo.findById(id);
			if (property.isPresent()) {
				try {
					propertyRepo.delete(property.get());
					return ResponseEntity.ok("Property is deleted!");
				} catch (Exception e) {
					return new ResponseEntity<>("Property could not be deleted!", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				return new ResponseEntity<>("Property not found.", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			System.out.println(String.format("Could not delete the property due to [%s] ", e.getMessage()));
			return new ResponseEntity<>("We are sorry, bad response!", HttpStatus.BAD_REQUEST);

		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(path = "/modify/{id}", produces = "application/json", consumes = "application/json")
	public ResponseEntity updatePropertyDetails(@PathVariable Integer id, @RequestBody Property request)
			throws Exception {
		try {

			Float propertyPrice = Float.valueOf(request.getPropertyPrice());
			String propertyType = request.getPropertyType();
			String propertyCategory = request.getPropertyCategory();
			String propertyName = request.getPropertyName();
			try {
				Optional<Property> option = propertyRepo.findById(Integer.valueOf(id));
				if (option.isPresent()) {
					Property currentProp = option.get();
					if (propertyPrice != null) {
						currentProp.setPropertyPrice((float) propertyPrice);
					}
					if (propertyType != null) {
						currentProp.setPropertyType(propertyType);
					}
					if (propertyCategory != null) {
						currentProp.setPropertyCategory(propertyCategory);
					}
					if (propertyName != null) {
						currentProp.setPropertyName(propertyName);
					}

					propertyRepo.save(currentProp);
				}

			} catch (Exception e) {
				System.out.println(String.format("Failed to modify the listing [%s] due to [%s]", id, e.getMessage()));
				return new ResponseEntity<>("Failed to modify the listing.", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("Listing is modified.", HttpStatus.CREATED);
		} catch (

		Exception e) {
			System.out.println(String.format("Could not modify the listing due to [%s]", e.getMessage()));
			return new ResponseEntity<>("We are sorry, bad response!", HttpStatus.BAD_REQUEST);

		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(path = "/register", produces = "application/json", consumes = "application/json")
	public ResponseEntity addProperty(@RequestBody Property request) throws Exception {
		HashMap<String, Object> status = new HashMap<String, Object>();
		CommonUtils utils = new CommonUtils();
		String properyUniqueId = "";
		Property property = new Property();

		try {
			int propertyOwnerId = request.getPropertyOwnerId();
			System.out.println(String.format("Property owner id [%s]", propertyOwnerId));
			List<Customer> clientInfo = customerRepo.findByClientId(propertyOwnerId);

			if (clientInfo.size() < 0 || propertyOwnerId < 0) {
				status.put("error", "Could not add property due to invalid user. Please signup!");
				status.put("errorCode", 1);
				return new ResponseEntity<>(status, HttpStatus.OK);
			} else {
				properyUniqueId = String.format("%s_%s", request.getPropertyName(),
						clientInfo.get(0).getClientEmailAddress());
				properyUniqueId = utils.base64Encode(properyUniqueId);
				if (propertyRepo.existsByPropertyUniqueId(properyUniqueId)) {
					status.put("error", "Duplicate listing!");
					status.put("errorCode", 2);
					return new ResponseEntity<>(status, HttpStatus.OK);
				} else {
					request.setPropertyUniqueId(properyUniqueId);
					BeanUtils.copyProperties(request, property);
					try {
						property = propertyRepo.save(property);
						status.put("errorCode", 0);
						return new ResponseEntity<>(status, HttpStatus.OK);
					} catch (Exception e) {
						throw new Exception("Could not process the listing" + e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			status.put("error", "Could not post the listing.");
			status.put("errorCode", 4);
			System.out.println(String.format("Could not post the listing due to [%s]", e.getMessage()));
			return new ResponseEntity<>(status, HttpStatus.OK);

		}
	}
	@GetMapping(value="/search", produces = "application/json")
	public List<Property> getPropertiesBySearch(@RequestBody PropertyRequest propertyRequest) {
		String queryString = "select * from Property where 1=1";
		queryString = checkForTheSearchField(queryString, propertyRequest);
		Query query = entityManager.createNativeQuery(queryString);
		List<Object[]> propertyList = query.getResultList();
		return formResponseObject(propertyList);
	}

	private List<Property> formResponseObject(List<Object[]> propertyList) {
		//0:propertyID, 1:pty_name, 2:pty_town, 3:pty_city, 4:pty_state, 5:pty_category, 6:pty_type,
		//7:pty_price, 8:pty_status, 9:pty_owner, 10:pty_unique, 11:pty_area, 12:pty_bed, 13:pty_country, 14:pty_zipcode, 15:pty_available_date, 16:pty_owner_id
		List<Property> responseObject = new ArrayList();
		for(int k=0;k<propertyList.size();k++) {
				Object[] pList = propertyList.get(k);
				Property response = new Property();
				
				response.setPropertyId(Integer.valueOf(safeConvertToString(pList[0])));
				response.setPropertyName(safeConvertToString(pList[1]));
				response.setPropertyTown(safeConvertToString(pList[2]));
				response.setPropertyCity(safeConvertToString(pList[3]));
				response.setPropertyState(safeConvertToString(pList[4]));
				response.setPropertyCategory(safeConvertToString(pList[5]));
				
				response.setPropertyType(safeConvertToString(pList[6]));
				response.setPropertyPrice(Float.parseFloat(safeConvertToString(pList[7])));
				response.setPropertyStatus(safeConvertToString(pList[8]));
				response.setPropertyOwner(safeConvertToString(pList[9]));
				response.setPropertyUniqueId(safeConvertToString(pList[10]));
				
				response.setPropertyArea(Integer.valueOf(safeConvertToString(pList[11])));
				response.setPropertyBed(Integer.valueOf(safeConvertToString(pList[12])));
				response.setPropertyCountry(safeConvertToString(pList[13]));
				response.setPropertyZipCode(Integer.valueOf(safeConvertToString(pList[14])));
				response.setPropertyAvailableDate((java.sql.Date) pList[15]);
				response.setPropertyOwnerId(Integer.valueOf(safeConvertToString(pList[16])));
				
				responseObject.add(response);
			
		}
		return responseObject;	
	}

	private static String safeConvertToString(Object obj) {
		return null != obj ? obj.toString() : null;
	}

	private String checkForTheSearchField(String queryString, PropertyRequest propertyRequest) {
		String query = queryString;
		
		if(null != propertyRequest.getPropertyId()) {
			query = query + " and propertyID = "+propertyRequest.getPropertyId();
		}
		if(null != propertyRequest.getPropertyName()) {
			query = query + " and pty_name = '"+propertyRequest.getPropertyName()+"'";
		}
		if(null != propertyRequest.getPropertyTown()) {
			query = query + " and pty_town = '"+propertyRequest.getPropertyTown()+"'";
		}
		if(null != propertyRequest.getPropertyCity()) {
			query = query + " and pty_city = '"+propertyRequest.getPropertyCity()+"'";
		}
		if(null != propertyRequest.getPropertyState()) {
			query = query + " and pty_state = '"+propertyRequest.getPropertyState()+"'";
		}
		if(null != propertyRequest.getPropertyCategory()) {
			query = query + " and pty_category = '"+propertyRequest.getPropertyCategory()+"'";
		}
		if(null != propertyRequest.getPropertyType()) {
			query = query + " and pty_type = '"+propertyRequest.getPropertyType()+"'";
		}
		if(0.0 != propertyRequest.getPropertyPrice()) {
			query = query + " and pty_price = '"+propertyRequest.getPropertyPrice()+"'";
		}
		if(null != propertyRequest.getPropertyStatus()) {
			query = query + " and pty_status = '"+propertyRequest.getPropertyStatus()+"'";
		}
		if(null != propertyRequest.getPropertyOwner()) {
			query = query + " and pty_owner = '"+propertyRequest.getPropertyOwner()+"'";
		}
		if(null != propertyRequest.getPropertyUniqueId()) {
			query = query + " and pty_unique = '"+propertyRequest.getPropertyUniqueId()+"'";
		}
		if(null != propertyRequest.getPropertyArea()) {
			query = query + " and pty_area = "+propertyRequest.getPropertyArea();
		}
		if(null != propertyRequest.getPropertyBed()) {
			query = query + " and pty_bed = "+propertyRequest.getPropertyBed();
		}
		if(null != propertyRequest.getPropertyCountry()) {
			query = query + " and pty_country = '"+propertyRequest.getPropertyCountry()+"'";
		}
		if(null != propertyRequest.getPropertyZipCode()) {
			query = query + " and pty_zipcode = "+propertyRequest.getPropertyZipCode();
		}
		if(null != propertyRequest.getPropertyAvailableDate()) {
			query = query + " and pty_available_date = '"+propertyRequest.getPropertyAvailableDate()+"'";
		}
		if(null != propertyRequest.getPropertyOwnerId()) {
			query = query + " and pty_owner_id = "+propertyRequest.getPropertyOwnerId();
		}
		return query;
	}

}