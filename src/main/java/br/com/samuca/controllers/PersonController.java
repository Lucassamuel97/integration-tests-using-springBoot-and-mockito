package br.com.samuca.controllers;

import java.awt.PageAttributes.MediaType;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.samuca.exceptions.ResourceNotFoundException;
import br.com.samuca.model.Person;
import br.com.samuca.services.PersonServices;

@RestController
@RequestMapping("/person")
public class PersonController {
	
	@Autowired
	private PersonServices service;
//	private PersonServices service = new PersonServices();
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Person> findAll(){
		return service.findAll();
	}
	
	@RequestMapping(value = "/{id}",
			method=RequestMethod.GET)
	public Person findById(@PathVariable(value = "id") String id){
		return service.findById(id);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Person create(@RequestBody Person person){
		return service.create(person);
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public Person update(@RequestBody Person person){
		return service.update(person);
	}
	
	@RequestMapping(value = "/{id}",
			method=RequestMethod.DELETE)
	public void delete(@PathVariable(value = "id") String id){
		 service.delete(id);
	}
}
