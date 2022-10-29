package com.bits.af.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bits.af.entities.Property;
import com.bits.af.entities.Transaction;
import com.bits.af.pojo.TransactionRequest;
import com.bits.af.repository.PropertyRepository;
import com.bits.af.repository.TransactionRepository;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	@Autowired
	private TransactionRepository repo;

	@Autowired
	private PropertyRepository propRepo;

	@GetMapping(produces = "application/json")
	public ResponseEntity<List<Transaction>> listAll() {
		List<Transaction> transactionInfo = repo.findAll();
		return ResponseEntity.ok(transactionInfo);
	}

	@PostMapping(path = "/list", produces = "application/json")
	public ResponseEntity<List<HashMap<String, Object>>> listAllTransactionsByUser(@RequestBody Transaction request) {
		int customerId = request.getClientId();
		List<Transaction> transactions = repo.findByClientId(customerId);
		List<HashMap<String, Object>> out = new ArrayList<HashMap<String, Object>>();
		System.out.println(transactions);
		for (Transaction transaction : transactions) {
			HashMap<String, Object> temp = new HashMap<String, Object>();
			int propertyId = transaction.getPropertyId();
			Optional<Property> propertyInfo = propRepo.findById(propertyId);
			if (propertyInfo.isPresent()) {
				Property prop = propertyInfo.get();
				temp.put("transactionId", transaction.getBookingId());
				temp.put("propertyName", prop.getPropertyName());
				temp.put("propertyCategory", prop.getPropertyCategory());
				temp.put("propertyType", prop.getPropertyType());
				temp.put("propertyPrice", prop.getPropertyPrice());
				temp.put("bookingId", transaction.getBookingId());
				temp.put("bookedOn", transaction.getTransactionStartDate());

			}

			out.add(temp);
		}
		return new ResponseEntity<>(out, HttpStatus.OK);
	}

	@GetMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<Transaction> listById(@PathVariable Integer id) {
		Optional<Transaction> transactions = repo.findById(id);

		if (transactions.isPresent())
			return ResponseEntity.ok(transactions.get());
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity performTransaction(@RequestBody TransactionRequest request) throws Exception {
		Transaction transaction = new Transaction();

		BeanUtils.copyProperties(request, transaction);
		try {
			transaction = repo.save(transaction);
			return new ResponseEntity<>("Transaction is completed", HttpStatus.CREATED);
		} catch (Exception e) {
			throw new Exception(String.format("Could not perform transaction due to [%s]", e.getMessage()));
		}
	}

}