package com.salaboy.dosomesql;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CustomerJsonTests {

	@Autowired
	private JacksonTester<Customer> json;

	@Test
	void testSerialize() throws Exception {
		var customer = new Customer(394L, "Spring", "I/O");
		var jsonContent = json.write(customer);
		assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
				.isEqualTo(customer.id().intValue());
		assertThat(jsonContent).extractingJsonPathStringValue("@.firstname")
				.isEqualTo(customer.firstname());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastname")
				.isEqualTo(customer.lastname());        
	}

	@Test
	void testDeserialize() throws Exception {
		var content = """
                {
                    "id": 394,
                    "firstname": "Spring",
                    "lastname": "I/O"
                }
                """;
		assertThat(json.parse(content))
				.usingRecursiveComparison()
				.isEqualTo(new Customer(394L, "Spring", "I/O"));
	}

}
