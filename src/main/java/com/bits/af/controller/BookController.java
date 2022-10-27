package com.bits.af.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bits.af.entities.Booking;
import com.bits.af.entities.Property;
import com.bits.af.entities.Transaction;
import com.bits.af.pojo.BookingRequest;
import com.bits.af.repository.BookRepository;
import com.bits.af.repository.PropertyRepository;
import com.bits.af.repository.TransactionRepository;

@RestController
@RequestMapping("/bookings")
public class BookController {
	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private PropertyRepository propertyRepo;
	
	@Autowired
	private TransactionRepository transactionRepo;

	@GetMapping(produces = "application/json")
	public ResponseEntity<List<Booking>> listAll() {
		List<Booking> bookingList = bookRepo.findAll();
		System.out.println(bookingList);
		return ResponseEntity.ok(bookingList);
	}

	@GetMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<Booking> listById(@PathVariable Integer id) {
		Optional<Booking> booking = bookRepo.findById(id);

		if (booking.isPresent())
			return ResponseEntity.ok(booking.get());
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	

	@Transactional
	@SuppressWarnings("rawtypes")
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity addBooking(@RequestBody BookingRequest bookingRequest) throws Exception {
		Booking book = new Booking();
		BeanUtils.copyProperties(bookingRequest, book);
		try {
			
			Optional<Property> property = propertyRepo.findById(book.getPropertyId());
			if(property.isPresent() && !property.get().getPropertyStatus().equals("B")) {
				property.get().setPropertyStatus("B");
				propertyRepo.save(property.get());
			}else if(property.get().getPropertyStatus().equals("B")) {
				throw new Exception("This property is already booked");
			}else {
				throw new Exception("Could not find property");
			}
			System.out.println(bookingRequest.getBookingId() + " "+ bookingRequest.getBookingStatus() + " "+ bookingRequest.getClientId() + " "+ bookingRequest.getPropertyId());
			book = bookRepo.save(book);
			Transaction transaction = new Transaction();
			transaction.setBookingId(book.getBookingId());
			transaction.setClientId(book.getClientId());
			transaction.setPropertyId(book.getPropertyId());
			transaction.setTransactionStartDate(Date.valueOf(LocalDate.now()));
			transaction.setTransactionStatus('P');
			transaction.setBankReferenceId("");
			transactionRepo.save(transaction);
			
			return new ResponseEntity<>("Booking successful", HttpStatus.CREATED);
		} catch (Exception e) {
			throw new Exception("Could not process booking " + e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping(produces = "application/json", consumes = "application/json")
	@Transactional
	public ResponseEntity updateBooking(@RequestBody BookingRequest bookingRequest) throws Exception {

		try {
			Optional<Booking> booking = bookRepo.findById(bookingRequest.getBookingId());
			if(booking.isPresent()) {
				booking.get().setBookingStatus(bookingRequest.getBookingStatus());
				Booking bookingInstance = booking.get();
				bookRepo.save(bookingInstance);
				if(bookingRequest.getBookingStatus() == 'C') {
					Transaction transaction = new Transaction();
					transaction.setBookingId(bookingInstance.getBookingId());
					transaction.setClientId(bookingInstance.getClientId());
					transaction.setPropertyId(bookingInstance.getPropertyId());
					transaction.setTransactionStartDate(Date.valueOf(LocalDate.now()));
					transaction.setTransactionStatus('C');
					
					transaction.setBankReferenceId(java.util.UUID.randomUUID().toString());
					transactionRepo.save(transaction);
				}
				return new ResponseEntity<>("Booking successful", HttpStatus.NO_CONTENT);
			}
			throw new Exception("Booking not found ");
		} catch (Exception e) {
			throw new Exception("Could not process booking " + e.getMessage());
		}
	}

}