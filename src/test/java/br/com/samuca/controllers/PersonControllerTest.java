package br.com.samuca.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.samuca.exceptions.ResourceNotFoundException;
import br.com.samuca.model.Person;
import br.com.samuca.services.PersonServices;

@WebMvcTest
public class PersonControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private PersonServices service;
	
	private Person person;
	
	@BeforeEach
	public void setup() {
		// Given / Arrange
		this.person = new Person("Lucas", "Samuca", "samuca@gmail.com.br", "Palmital - Parana - Brasil", "Male");
	}
	
	@Test
    @DisplayName("JUnit test for Given Person Object when Create Person then Return Saved Person")
    void testGivenPersonObject_WhenCreatePerson_thenReturnSavedPerson() throws JsonProcessingException, Exception {
        
        // Given / Arrange
        given(service.create(any(Person.class)))
            .willAnswer((invocation) -> invocation.getArgument(0));
        
        // When / Act
        ResultActions response = mockMvc.perform(post("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(person)));
        
        // Then / Assert
        //Imprime o JSON e verifica se o conteudo é o esperado
        response.andDo(print()).
            andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
            .andExpect(jsonPath("$.lastName", is(person.getLastName())))
            .andExpect(jsonPath("$.email", is(person.getEmail())));
    }
	
	@Test
	@DisplayName("JUnit test for Given List Persons when FindAll then Return Persons List")
	void testGivenListOfPersons_WhenFindAll_thenReturnListPersons() throws JsonProcessingException, Exception {
		
		// Given / Arrange
        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(new Person(
                "Lucas",
                "samuel",
                "lucas@gmail.com.br",
                "Palmital - Parana - Brasil",
                "Male"));
        
        given(service.findAll()).willReturn(persons);
        
        // When / Act
        ResultActions response = mockMvc.perform(get("/person"));
        
        // Then / Assert
        response.
            andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.size()", is(persons.size())));
	}
	
	@Test
    @DisplayName("JUnit test for Given Person ID whenFindById then Return Person Object")
    void testGivenPersonID_WhenFindById_thenReturnPersonObject() throws JsonProcessingException, Exception {
        
        // Given / Arrange
		long personId = 1L;
		
        given(service.findById(personId)).willReturn(person);
        
        // When / Act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));
        
        // Then / Assert
        response.andDo(print()).
            andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
            .andExpect(jsonPath("$.lastName", is(person.getLastName())))
            .andExpect(jsonPath("$.email", is(person.getEmail())));
    }
	
	
	@Test
    @DisplayName("JUnit test for Given invalid Person ID when FindById then Return Not Found")
    void testGivenInvalidPersonID_WhenFindById_thenReturnNotFound() throws JsonProcessingException, Exception {
        
        // Given / Arrange
		long personId = 1L;
		
        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
        
        // When / Act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));
        
        // Then / Assert
        response.
        andExpect(status().isNotFound())
        .andDo(print());
    }
	
	@Test
    @DisplayName("JUnit test for Given Update Person when update then Return Update person Object")
    void testGivenUpdatePerson_WhenUpdate_thenReturnUpdatePersonObject() throws JsonProcessingException, Exception {
        
        // Given / Arrange
		long personId = 1L;
		given(service.findById(personId)).willReturn(person);
		given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));
        
        // When / Act
		Person updatedPerson = new Person(
				"Lucas",
                "samuel",
                "lucas@gmail.com.br",
                "Palmital - Parana - Brasil",
                "Male");
		
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));
        
        // Then / Assert
        response.andDo(print()).
            andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
            .andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
            .andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
    }
	
	@Test
    @DisplayName("JUnit test for Given Unexistent Person when update then Return Not Found")
    void testGivenUnexistentPerson_WhenUpdate_thenReturnNotFound() throws JsonProcessingException, Exception {
        
        // Given / Arrange
		
		long personId = 1L;
		
		//Configura o comportamento do serviço findById para lançar uma exceção ResourceNotFoundException quando chamado com o ID da pessoa.
		given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
		//Configura o comportamento do serviço update para retornar o segundo argumento passado para ele. Este comportamento será usado se a atualização for chamada durante o teste.
		given(service.update(any(Person.class)))
		.willAnswer((invocation) -> invocation.getArgument(1));
        
        // When / Act
		Person updatedPerson = new Person(
				"Lucas",
                "samuel",
                "lucas@gmail.com.br",
                "Palmital - Parana - Brasil",
                "Male");
		
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));
        
        // Then / Assert
        response.
        andExpect(status().isNotFound())
        .andDo(print());
    }
	
	 @Test
	    @DisplayName("JUnit test for Given personId when Delete then Return NotContent")
	    void testGivenPersonId_WhenDelete_thenReturnNotContent() throws JsonProcessingException, Exception {
	        
	        // Given / Arrange
	        long personId = 1L;
	        
	        // Configura o serviço para não fazer nada (não lançar exceções, nem realizar operações) quando o método delete é chamado com o ID da pessoa.
	        willDoNothing().given(service).delete(personId);
	        
	        // When / Act
	        ResultActions response = mockMvc.perform(delete("/person/{id}", personId));
	        
	        // Then / Assert
	        response.
	            andExpect(status().isNoContent())
	                .andDo(print());
	    }
}
