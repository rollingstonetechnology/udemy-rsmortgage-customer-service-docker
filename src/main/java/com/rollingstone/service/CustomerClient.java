package com.rollingstone.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rollingstone.domain.Customer;


@FeignClient("udemy-rsmortgage-customer-service")
interface CustomerClient {
	
	
	@RequestMapping(method = RequestMethod.GET, value="/rsmortgage-customerservice/v1/customer/all")
	List<Customer> getCustomers();
	
	@RequestMapping(method = RequestMethod.GET, value="/rsmortgage-customerservice/v1/customer/simple/{id}")
	Customer getCustomer(@PathVariable("id") Long id);
	
}