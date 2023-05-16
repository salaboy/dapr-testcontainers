package com.salaboy.dosomesql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "customer", path = "customer")
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, CrudRepository<Customer,Long> {

	List<Customer> findByLastName(@Param("name") String name);
  Optional<Customer> findById(@Param("id") Long id);

}
