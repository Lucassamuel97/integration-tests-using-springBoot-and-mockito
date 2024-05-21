package br.com.samuca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.samuca.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {}

