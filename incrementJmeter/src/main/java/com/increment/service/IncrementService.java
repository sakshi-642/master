package com.increment.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.increment.entity.Number;
import com.increment.repository.IncrementRepository;

@Service
public class IncrementService  {
	
	Logger logger = LoggerFactory.getLogger(IncrementService.class);

	@Autowired
	IncrementRepository incrementRepository;
	
	/**
	 * Increasing the value of counter.
	 * Transactional annotation used to ensure all the process involved in the function executes as single transaction or it gets rollback.
	 * Pessimistic locking is used to ensure to get a write lock on the counter, while fetching it from database.
	 
	 * @param id
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void incrementCounter(Long id) {
		try {
			Optional<Number> optionalNumber = incrementRepository.findById(id);
			if (optionalNumber.isPresent()) {
				Number number = optionalNumber.get();
				number.setCounter(number.getCounter() + 1);
				incrementRepository.save(number);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while incrementing counter", e);
			throw e;
		}

	}

	
	public void setInitialValue(Long id) {
		try {
			boolean isAlreadyExist = incrementRepository.existsById(id);
			if(!isAlreadyExist) {
				Number number = new Number();
				number.setId(id);
				incrementRepository.save(number);
			} else {
				logger.info("Number already exist with the id " + id);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while setting the initial value of number", e);
			throw e;
		}
		
	}

}
