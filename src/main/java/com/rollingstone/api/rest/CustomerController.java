package com.rollingstone.api.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rollingstone.domain.Customer;
import com.rollingstone.exception.HTTP400Exception;
import com.rollingstone.service.RsMortgageCustomerService;
import com.rollingstone.service.ServiceEvent;

/*
* Demonstrates how to set up RESTful API endpoints using Spring MVC
*/
@RestController
@RequestMapping(value = "/rsmortgage-customerservice/v1/customer")
public class CustomerController extends AbstractRestController {

	@Autowired
	private RsMortgageCustomerService customerService;
	
	@Autowired
	CounterService counterService;

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = { "application/json",
			"application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.CREATED)
	public void createCustomer(@RequestBody Customer customer, HttpServletRequest request,
			HttpServletResponse response) {
		Customer createdCustomer = this.customerService.createCustomer(customer);
		if (createdCustomer != null){
			counterService.increment("com.rollingstone.Customer.created.success");
			eventPublisher.publishEvent(new ServiceEvent(this, createdCustomer, "CustomerCreated" ));
		}else {
			counterService.increment("com.rollingstone.Customer.created.failure");
		}
		response.setHeader("Location", request.getRequestURL().append("/").append(createdCustomer.getId()).toString());
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Page<Customer> getAllCustomersByPage(
			@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
			@RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
			HttpServletRequest request, HttpServletResponse response) {
		return this.customerService.getAllCustomersByPage(page, size);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<Customer> getAllCustomers(
			@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
			@RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
			HttpServletRequest request, HttpServletResponse response) {
		return this.customerService.getAllCustomers();
	}

	@RequestMapping("/simple/{id}")
	public Customer getSimpleCustomer(@PathVariable("id") Long id) {
		Customer customer = this.customerService.getCustomer(id);
		checkResourceFound(customer);
		return customer;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody

			Customer getCustomer(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		Customer customer = this.customerService.getCustomer(id);
		checkResourceFound(customer);
		return customer;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = { "application/json",
			"application/xml" }, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer, HttpServletRequest request,
			HttpServletResponse response) {
		checkResourceFound(this.customerService.getCustomer(id));
		if (id != customer.getId())
			throw new HTTP400Exception("ID doesn't match!");
		counterService.increment("com.rollingstone.Customer.updated.success");
		
		this.customerService.updateCustomer(customer);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = { "application/json",
			"application/xml" })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCustomer(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
		checkResourceFound(this.customerService.getCustomer(id));
		counterService.increment("com.rollingstone.Customer.deleted.success");
		this.customerService.deleteCustomer(id);
	}
}
