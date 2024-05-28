package br.com.samuca.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.samuca.config.TestConfigs;
import br.com.samuca.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.samuca.model.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static Person person;

	@BeforeAll
	public static void setup() {

		// Given / Arrange
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		specification = new RequestSpecBuilder().setBasePath("/person").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		person = new Person("Lucas", "Samuel", "lukassamuka88@gmail.com", "Palmital - Parana - Brasil", "Male");
	}

	@Test
	@Order(1)
	@DisplayName("JUnit integration given Person Object when Create one Person should Return a Person Object")
	void integrationTestGivenPersonObject_when_CreateOnePerson_ShouldReturnAPersonObject()
			throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
            .when()
                .post()
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();

		// Convertendo resposta de JSON para object Person
		Person createdPerson = objectMapper.readValue(content, Person.class);

		// Para salvar o Id na person para utilizarmos nos proximos testes
		person = createdPerson;

		assertNotNull(createdPerson);

		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertNotNull(createdPerson.getEmail());

		assertTrue(createdPerson.getId() > 0);
		assertEquals("Lucas", createdPerson.getFirstName());
		assertEquals("Samuel", createdPerson.getLastName());
		assertEquals("Palmital - Parana - Brasil", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		assertEquals("lukassamuka88@gmail.com", createdPerson.getEmail());
	}

	@Test
	@Order(2)
	@DisplayName("JUnit integration given Person Object when Update one Person should Return a Updated Person Object")
	void integrationTestGivenPersonObject_when_UpdateOnePerson_ShouldReturnAUpdatedPersonObject()
			throws JsonMappingException, JsonProcessingException {

		person.setFirstName("Lucas Update");
		person.setEmail("lucassamuel@gmail.com.br");

		var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
            .when()
                .put()
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();

		Person updatedPerson = objectMapper.readValue(content, Person.class);

		person = updatedPerson;

		assertNotNull(updatedPerson);

		assertNotNull(updatedPerson.getId());
		assertNotNull(updatedPerson.getFirstName());
		assertNotNull(updatedPerson.getLastName());
		assertNotNull(updatedPerson.getAddress());
		assertNotNull(updatedPerson.getGender());
		assertNotNull(updatedPerson.getEmail());

		assertTrue(updatedPerson.getId() > 0);
		assertEquals("Lucas Update", updatedPerson.getFirstName());
		assertEquals("Samuel", updatedPerson.getLastName());
		assertEquals("Palmital - Parana - Brasil", updatedPerson.getAddress());
		assertEquals("Male", updatedPerson.getGender());
		assertEquals("lucassamuel@gmail.com.br", updatedPerson.getEmail());
	}
	
	@Test
    @Order(3)
    @DisplayName("JUnit integration given Person Object when findById should Return a Person Object")
    void integrationTestGivenPersonObject_when_findById_ShouldReturnAPersonObject() throws JsonMappingException, JsonProcessingException {
        
        var content = given().spec(specification)
                .pathParam("id", person.getId())
            .when()
                .get("{id}")
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();
        
        Person foundPerson = objectMapper.readValue(content, Person.class);
        
        assertNotNull(foundPerson);
        
        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());
        assertNotNull(foundPerson.getEmail());
        
        assertTrue(foundPerson.getId() > 0);
        assertEquals("Lucas Update", foundPerson.getFirstName());
        assertEquals("Samuel", foundPerson.getLastName());
        assertEquals("Palmital - Parana - Brasil", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertEquals("lucassamuel@gmail.com.br", foundPerson.getEmail());
    }
	
	@Test
    @Order(4)
    @DisplayName("JUnit integration given Person Object when findAll should Return a Persons List")
    void integrationTestGiven_when_findAll_ShouldReturnAPersonsList() throws JsonMappingException, JsonProcessingException {
        
		Person anotherPerson = new Person(
                "Ana",
                "lucia",
                "ana@gmail.com.br",
                "Palmital - Parana - Brasil",
                "Female"
            );
		
		given().spec(specification)
        .contentType(TestConfigs.CONTENT_TYPE_JSON)
        .body(anotherPerson)
        	.when()
        .post()
        	.then()
        		.statusCode(200);
		
		var content = given().spec(specification)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();
        
		Person[] myArray = objectMapper.readValue(content, Person[].class);
        List<Person> people = Arrays.asList(myArray);
        
        Person foundPersonOne = people.get(0);
        
        assertNotNull(foundPersonOne);
        
        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
        assertNotNull(foundPersonOne.getEmail());
        
        assertTrue(foundPersonOne.getId() > 0);
        assertEquals("Lucas Update", foundPersonOne.getFirstName());
        assertEquals("Samuel", foundPersonOne.getLastName());
        assertEquals("Palmital - Parana - Brasil", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());
        assertEquals("lucassamuel@gmail.com.br", foundPersonOne.getEmail());
        
        Person foundPersonTwo = people.get(1);
        
        assertNotNull(foundPersonTwo);
        
        assertNotNull(foundPersonTwo.getId());
        assertNotNull(foundPersonTwo.getFirstName());
        assertNotNull(foundPersonTwo.getLastName());
        assertNotNull(foundPersonTwo.getAddress());
        assertNotNull(foundPersonTwo.getGender());
        assertNotNull(foundPersonTwo.getEmail());
        
        assertTrue(foundPersonTwo.getId() > 0);
        assertEquals("Ana", foundPersonTwo.getFirstName());
        assertEquals("lucia", foundPersonTwo.getLastName());
        assertEquals("Palmital - Parana - Brasil", foundPersonTwo.getAddress());
        assertEquals("Female", foundPersonTwo.getGender());
        assertEquals("ana@gmail.com.br", foundPersonTwo.getEmail());
        
    }
	
	@Test
    @Order(5)
    @DisplayName("JUnit integration given Person Object when delete should Return No Content")
    void integrationTestGivenPersonObject_when_delete_ShouldReturnNoContent() throws JsonMappingException, JsonProcessingException {
        
        given().spec(specification)
                .pathParam("id", person.getId())
            .when()
                .delete("{id}")
            .then()
                .statusCode(204);
    }
}
