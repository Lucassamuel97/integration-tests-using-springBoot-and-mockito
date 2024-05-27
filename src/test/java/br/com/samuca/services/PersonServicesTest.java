package br.com.samuca.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.samuca.exceptions.ResourceNotFoundException;
import br.com.samuca.model.Person;
import br.com.samuca.repositories.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

	@Mock
	private PersonRepository repository;
	
	@InjectMocks
	private PersonServices services;
	
	private Person person0;
	
	@BeforeEach
	public void setup() {
		// Given / Arrange
		this.person0 = new Person("Lucas", "Samuca", "samuca@gmail.com.br", "Palmital - Parana - Brasil", "Male");
	}
	
	@DisplayName("JUnit test for Given Person Object when Save Person then Return Person Object")
    @Test
    void testGivenPersonObject_WhenSavePerson_thenReturnPersonObject() {

		// Given / Arrange
		given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person0)).willReturn(person0);
        
		// When / Act
        Person savedPerson = services.create(person0);

		// Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Lucas", savedPerson.getFirstName());
	}
	
	@DisplayName("JUnit test for Given Existing email when Save Person then therows Exception")
    @Test
    void testGivenExistingEmail_WhenSavePerson_thenReturnPersonObject(){

		// Given / Arrange
		given(repository.findByEmail(anyString())).willReturn(Optional.of(person0));
        
		// When / Act
		assertThrows(ResourceNotFoundException.class, () -> {
            services.create(person0);
        });

		// Then / Assert
		//Verificando com o mockito verify que nunca deve ser chamado o metodo save de qualquer estancia de Person.class
		verify(repository, never()).save(any(Person.class));
	}
	
	@DisplayName("JUnit test for Given Persons List when findAll then Persons List")
	@Test
	void testGivenPersonsList_WhenFindAllPersons_thenReturnPersonsList(){
		
		// Given / Arrange
		Person person1 = new Person("Ana", "teste", "teste@teste.com.br", "Palmita - Parana - Brasil", "Female");

		given(repository.findAll()).willReturn(List.of(person0,person1));
		
		// When / Act
		List<Person> personsList = services.findAll();
		
		// Then / Assert
		assertNotNull(personsList);
		assertEquals(2, personsList.size());
	}
	
	@DisplayName("JUnit test for Given Empty Persons List when findAll then Empty Persons List")
	@Test
	void testGivenEmptyPersonsList_WhenFindAllPersons_thenReturnEmptyPersonsList(){
		
		// Given / Arrange
		given(repository.findAll()).willReturn(Collections.emptyList());
		
		// When / Act
		List<Person> personsList = services.findAll();
		
		// Then / Assert
		assertTrue(personsList.isEmpty());
		assertEquals(0, personsList.size());
	}
	
	@DisplayName("JUnit test for Given Person Id when FindById then Return Person Object")
    @Test
    void testGivenPersonId_WhenFindById_thenReturnPersonObject() {

		// Given / Arrange
		//Quando o metodo findById for invocado com qualquer long, retornara o Person0
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
        
		// When / Act
        Person savedPerson = services.findById(1L);

		// Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Lucas", savedPerson.getFirstName());
	}
	
	@DisplayName("JUnit test for Given Person Object when Update Person then Return Person Object")
	@Test
	void testGivenPersonObject_WhenUpdatePerson_thenReturnPersonObject() {
		
		// Given / Arrange
		person0.setId(1L);
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
		
		person0.setEmail("samucaUpdate@gmail.com.br");
		person0.setFirstName("Lucas Update");
		
		given(repository.save(person0)).willReturn(person0);
		
		// When / Act
		Person updatePerson = services.update(person0);
		
		// Then / Assert
		assertNotNull(updatePerson);
		assertEquals("Lucas Update", updatePerson.getFirstName());
		assertEquals("samucaUpdate@gmail.com.br", updatePerson.getEmail());
	}
	
	@DisplayName("JUnit test for Given PersonID when Delete Person then do Nothing")
	@Test
	void testGivenPersonID_WhenDeletePerson_thenDoNothing() {
		
		// Given / Arrange
		person0.setId(1L);
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
		willDoNothing().given(repository).delete(person0);
		
		// When / Act
		services.delete(person0.getId());
		
		// Then / Assert
		verify(repository, times(1)).delete(person0);
	}
	
}
