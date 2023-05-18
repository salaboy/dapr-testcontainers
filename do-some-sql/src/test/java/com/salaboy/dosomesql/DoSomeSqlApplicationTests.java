package com.salaboy.dosomesql;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestEnvironmentConfiguration.class)
class DoSomeSqlApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void whenGetRequestWithIdThenCustomerReturned() {
		var customerToCreate = new Customer(null, "Spring", "I/O");
		Customer expectedCustomer = webTestClient
				.post()
				.uri("/customers")
				.bodyValue(customerToCreate)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Customer.class).value(customer -> assertThat(customer).isNotNull())
				.returnResult().getResponseBody();

		webTestClient
				.get()
				.uri("/customers/" + expectedCustomer.id())
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(Customer.class).value(actualCustomer -> {
					assertThat(actualCustomer).isNotNull();
					assertThat(actualCustomer.firstname()).isEqualTo(expectedCustomer.firstname());
					assertThat(actualCustomer.lastname()).isEqualTo(expectedCustomer.lastname());
				});
	}

}
