package com.example.sbdatajpademo.controller;

import com.example.sbdatajpademo.entity.Person;
import com.example.sbdatajpademo.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    @GetMapping
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/{personId}")
    public Person getPerson(@PathVariable Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));
    }

//    @PostConstruct
//    public void init() {
//        for (int i = 1; i < 100; i++) {
//            Person person = new Person();
//            person.setFistName("FirstName" + i);
//            person.setLastName("LastName" + i);
//            person.setAge(i);
//
//            personRepository.save(person);
//        }
//    }

}
