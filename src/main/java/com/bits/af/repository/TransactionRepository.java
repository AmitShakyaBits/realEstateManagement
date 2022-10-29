package com.bits.af.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bits.af.entities.Property;
import com.bits.af.entities.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	
	List<Property> findBybookingId(@Param("bookingId") int bookingId);
	List<Transaction> findByClientId(@Param("clientId") int clientId);

}