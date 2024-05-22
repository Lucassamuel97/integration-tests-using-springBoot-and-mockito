package br.com.samuca.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.samuca.model.Person;

@DataJpaTest
class PersonRepositoryTest {

	@Autowired
	private PersonRepository repository;
	
	private Person person0;

	@BeforeEach
	public void setup() {
		// Given / Arrange
		this.person0 = new Person("Lucas", "Samuca", "samuca@gmail.com.br", "Palmital - Parana - Brasil", "Male");
	}

	@DisplayName("JUnit test for Given Person Object when Save then Return Saved Person")
	@Test
	void testGivenPersonObject_whenSave_thenReturnSavedPerson() {

		// Given / Arrange

		// When / Act
		Person savedPerson = repository.save(person0);

		// Then / Assert
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() > 0);
	}

	@DisplayName("JUnit test for Given Person List when findAll then Return Person List")
	@Test
	void testGivenPersonList_whenFindAll_thenReturnPersonList() {

		// Given / Arrange

		Person person1 = new Person("Ana", "teste", "teste@teste.com.br", "Palmita - Parana - Brasil", "Female");

		repository.save(person0);
		repository.save(person1);

		// When / Act
		List<Person> personList = repository.findAll();

		// Then / Assert
		assertNotNull(personList);
		assertEquals(2, personList.size());
	}

	@DisplayName("JUnit test for Given Person Object when findByID then Return Person Object")
	@Test
	void testGivenPersonObject_whenFindByID_thenReturnPersonObject() {

		// Given / Arrange
		repository.save(person0);

		// When / Act
		Person savedPerson = repository.findById(person0.getId()).get();

		// Then / Assert
		assertNotNull(savedPerson);
		assertEquals(person0.getId(), savedPerson.getId());
	}

	@DisplayName("JUnit test for Given Person Object when findByEmail then Return Person Object")
	@Test
	void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {

		// Given / Arrange
		repository.save(person0);

		// When / Act
		Person savedPerson = repository.findByEmail(person0.getEmail()).get();

		// Then / Assert
		assertNotNull(savedPerson);
		assertEquals(person0.getId(), savedPerson.getId());
	}

	@DisplayName("Given Person Object when Update Person then Return Updated Person Object")
	@Test
	void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {

		// Given / Arrange
		repository.save(person0);

		// When / Act
		Person savedPerson = repository.findById(person0.getId()).get();
		savedPerson.setFirstName("Samuca teste");
		savedPerson.setEmail("samucaaaa@gmail.com.br");

		Person updatedPerson = repository.save(savedPerson);

		// Then / Assert
		assertNotNull(updatedPerson);
		assertEquals("Samuca teste", updatedPerson.getFirstName());
		assertEquals("samucaaaa@gmail.com.br", updatedPerson.getEmail());
	}

	@DisplayName("Given Person Object when Delete then Remove Person")
	@Test
	void testGivenPersonObject_whenDelete_thenRemovePerson() {

		// Given / Arrange
		repository.save(person0);

		// When / Act
		repository.deleteById(person0.getId());

		Optional<Person> personOptional = repository.findById(person0.getId());

		// Then / Assert
		assertTrue(personOptional.isEmpty());
	}

	@DisplayName("JUnit test for Given firstName and lastName when findJPQL then Return Person Object")
	@Test
	void testGivenFirstNameAndLastName_whenFindJPQL_thenReturnPersonObject() {

		// Given / Arrange
		repository.save(person0);

		String firstName = "Lucas";
		String lastName = "Samuca";

		// When / Act
		Person savedPerson = repository.findByJPQL(firstName, lastName);

		// Then / Assert
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(lastName, savedPerson.getLastName());
	}

	@DisplayName("JUnit test for Given firstName and lastName when findJPQL Named Parameters then Return Person Object")
	@Test
	void testGivenFirstNameAndLastName_whenFindJPQLNamedParameters_thenReturnPersonObject() {

		// Given / Arrange
		repository.save(person0);

		String firstName = "Lucas";
		String lastName = "Samuca";

		// When / Act
		Person savedPerson = repository.findByJPQLNamedParameters(firstName, lastName);

		// Then / Assert
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(lastName, savedPerson.getLastName());
	}

	@DisplayName("JUnit test for Given firstName and lastName when whenFindByNativeSQL then Return Person Object")
	@Test
	void testGivenFirstNameAndLastName_whenFindByNativeSQL_thenReturnPersonObject() {

		// Given / Arrange
		repository.save(person0);

		String firstName = "Lucas";
		String lastName = "Samuca";

		// When / Act
		Person savedPerson = repository.findByJPQLNativeSQL(firstName, lastName);

		// Then / Assert
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(lastName, savedPerson.getLastName());
	}

	@DisplayName("JUnit test for Given firstName and lastName when whenFindByNativeSQLNamedParameters then Return Person Object")
	@Test
	void testGivenFirstNameAndLastName_whenFindByNativeSQLNamedParameters_thenReturnPersonObject() {

		// Given / Arrange
		repository.save(person0);

		String firstName = "Lucas";
		String lastName = "Samuca";

		// When / Act
		Person savedPerson = repository.findByJPQLNativeSQLNamedParameters(firstName, lastName);

		// Then / Assert
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(lastName, savedPerson.getLastName());
	}
}
