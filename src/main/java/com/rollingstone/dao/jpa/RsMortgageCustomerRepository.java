package com.rollingstone.dao.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.rollingstone.domain.Customer;

public interface RsMortgageCustomerRepository extends PagingAndSortingRepository<Customer, Long> {
	Customer findCustomerByRating(int rating);

	Page findAll(Pageable pageable);
}
