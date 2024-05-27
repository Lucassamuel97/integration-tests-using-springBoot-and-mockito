package br.com.samuca.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.samuca.exceptions.ResourceNotFoundException;
import br.com.samuca.model.Person;
import br.com.samuca.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	public List<Person> findAll(){
		logger.info("Finding all people!");

		return repository.findAll();
	}
	
	public Person findById(Long id){
		logger.info("Finding one person!");
		
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	}
	
	public Person create(Person person) {

		logger.info("Creating one person!");
		
		Optional<Person> savedPerson = repository.findByEmail(person.getEmail());
		if(savedPerson.isPresent()) {
		    throw new ResourceNotFoundException(
	            "Person already exist with given e-Mail: " + person.getEmail());
		}
		return repository.save(person);
	}
	
	public Person update(Person person){
		logger.info("Update one person!");
		
		Person entity = repository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		return repository.save(entity);
	}
	
	public void delete(Long id){
		logger.info("deleting one person!");
		
		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		repository.delete(entity);
	}
}
