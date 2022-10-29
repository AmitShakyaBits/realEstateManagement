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
/*import com.bits.af.pojo.PropertyRequest;*/
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
	public ResponseEntity<String> deleteProperty(@PathVariable Integer id) throws Exception {
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
	public ResponseEntity<String> updatePropertyDetails(@PathVariable Integer id, @RequestBody Property request)
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
	public ResponseEntity<HashMap<String, Object>> addProperty(@RequestBody Property request) throws Exception {
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

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/search", produces = "application/json")
	public ResponseEntity<List<Property>> getPropertiesBySearch(@RequestBody Property propertyRequest) {
		String queryString = "select * from Property where 1=1";
		List<Object[]> propertyList = new ArrayList<Object[]>();
		try {
			queryString = checkForTheSearchField(queryString, propertyRequest);
			Query query = entityManager.createNativeQuery(queryString);
			propertyList = query.getResultList();
		} catch (Exception e) {
			return new ResponseEntity<>(formResponseObject(propertyList), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(formResponseObject(propertyList), HttpStatus.OK);
	}

	private List<Property> formResponseObject(List<Object[]> propertyList) {
		List<Property> responseObject = new ArrayList<Property>();
		if (propertyList.size() <= 0) {
			return responseObject;
		}
		for (int k = 0; k < propertyList.size(); k++) {
			Object[] pList = propertyList.get(k);
			Optional<Property> property = propertyRepo.findById(Integer.valueOf(safeConvertToString(pList[0])));
			if (property.isPresent())
				responseObject.add(property.get());
		}
		return responseObject;
	}

	private static String safeConvertToString(Object obj) {
		return null != obj ? obj.toString() : null;
	}

	private String checkForTheSearchField(String queryString, Property propertyRequest) {
		String query = queryString;
		if (propertyRequest.getPropertyBed() <= 0)
			propertyRequest.setPropertyBed(null);

		if (propertyRequest.getPropertyType().equals("Any"))
			propertyRequest.setPropertyType(null);

		if (null != propertyRequest.getPropertyId()) {
			query = query + " and property_id = " + propertyRequest.getPropertyId();
		}
		if (null != propertyRequest.getPropertyName()) {
			query = query + " and pty_name like '%" + propertyRequest.getPropertyName() + "%'";
		}
		if (null != propertyRequest.getPropertyTown()) {
			query = query + " and pty_town = '" + propertyRequest.getPropertyTown() + "'";
		}
		if (null != propertyRequest.getPropertyCity()) {
			query = query + " and pty_city like '%" + propertyRequest.getPropertyCity() + "%'";
		}
		if (null != propertyRequest.getPropertyState()) {
			query = query + " and pty_state = '" + propertyRequest.getPropertyState() + "'";
		}
		if (null != propertyRequest.getPropertyCategory()) {
			query = query + " and pty_category = '" + propertyRequest.getPropertyCategory() + "'";
		}
		if (null != propertyRequest.getPropertyType()) {
			query = query + " and pty_type = '" + propertyRequest.getPropertyType() + "'";
		}
		if (0.0 != propertyRequest.getPropertyPrice()) {
			query = query + " and pty_price = '" + propertyRequest.getPropertyPrice() + "'";
		}
		if (null != propertyRequest.getPropertyStatus()) {
			query = query + " and pty_status = '" + propertyRequest.getPropertyStatus() + "'";
		}
		if (null != propertyRequest.getPropertyOwner()) {
			query = query + " and pty_owner = '" + propertyRequest.getPropertyOwner() + "'";
		}
		if (null != propertyRequest.getPropertyUniqueId()) {
			query = query + " and pty_unique = '" + propertyRequest.getPropertyUniqueId() + "'";
		}
		if (null != propertyRequest.getPropertyArea()) {
			query = query + " and pty_area = " + propertyRequest.getPropertyArea();
		}
		if (null != propertyRequest.getPropertyBed()) {
			query = query + " and pty_bed = " + propertyRequest.getPropertyBed();
		}
		if (null != propertyRequest.getPropertyCountry()) {
			query = query + " and pty_country = '" + propertyRequest.getPropertyCountry() + "'";
		}
		if (null != propertyRequest.getPropertyZipCode()) {
			query = query + " and pty_zipcode = " + propertyRequest.getPropertyZipCode();
		}
		if (null != propertyRequest.getPropertyAvailableDate()) {
			query = query + " and pty_available_date = '" + propertyRequest.getPropertyAvailableDate() + "'";
		}
		if (null != propertyRequest.getPropertyOwnerId()) {
			query = query + " and pty_owner_id = " + propertyRequest.getPropertyOwnerId();
		}
		return query;
	}

}