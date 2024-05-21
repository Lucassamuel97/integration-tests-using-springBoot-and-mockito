package br.com.samuca.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import br.com.samuca.model.Person;

@Service
public class PersonServices {
	
	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	public List<Person> findAll(){
		logger.info("Finding all people!");
		List<Person> persons = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			Person person = mockPerson(i);
			persons.add(person);
		}
		return persons ;
	}
	
	public Person findById(String id){
		logger.info("Finding one person!");
		
		Person person =  new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Lucas");
		person.setLastName("Samuel");
		person.setAddress("Palmital");
		person.setGender("Male");
		return person;
	}
	
	public Person create(Person person){
		logger.info("Creating one person!");
		return person;
	}
	
	public Person update(Person person){
		logger.info("Update one person!");
		return person;
	}
	
	public void delete(String id){
		logger.info("deleting one person!");
	}
	
	private Person mockPerson(int i) {
		Person person =  new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Person Name" + i);
		person.setLastName("lastname" + i);
		person.setAddress("Address " + i);
		person.setGender("Male");
		return person;
	}
}
