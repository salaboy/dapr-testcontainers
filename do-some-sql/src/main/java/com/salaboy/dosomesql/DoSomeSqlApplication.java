package com.salaboy.dosomesql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@SpringBootApplication
public class DoSomeSqlApplication {

	private static final Logger log = LoggerFactory.getLogger(DoSomeSqlApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DoSomeSqlApplication.class, args);
	}
}

@RestController
@RequestMapping("/customers")
class CustomerController {
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	
	private final CustomerRepository customerRepository;

	CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@GetMapping
	List<Customer> getCustomers() {
		log.info("Retrieving all customers");
		return customerRepository.findAll();
	}

	@GetMapping("{id}")
	Customer getCustomerById(@PathVariable Long id) {
		log.info("Retrieving customer by id: {}", id);
		return customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Customer addCustomer(@RequestBody @Valid Customer customer) {
		log.info("Adding new customer: {}", customer.firstname());
		return customerRepository.save(customer);
	}
}

class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException(Long id) {
		super("Customer with id " + id + " not found.");
	}
}

record Customer(@Id Long id, @NotEmpty String firstname, @NotEmpty String lastname) {
}

@Repository
interface CustomerRepository extends ListCrudRepository<Customer, Long> {}