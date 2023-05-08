package com.example.sbdatajpademo.controller;

import com.example.sbdatajpademo.entity.Person;
import com.example.sbdatajpademo.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/byFirstName")
    public List<Person> getByFirstName(@RequestParam(value = "firstName", required = false) String firstName) {
        return personRepository.findByName(firstName);
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

/*
    `@RequestParam` - це анотація в Spring Framework, яка дозволяє вам отримувати параметри запиту з URL-адреси.
    Вона може бути використана для отримання параметрів запиту GET або POST.

    Основні атрибути `@RequestParam`:

        - `value` - назва параметра запиту. Якщо не вказано, використовується ім'я параметра методу.
        - `required` - чи є параметр обов'язковим. За замовчуванням `true`.
        - `defaultValue` - значення за замовчуванням, яке буде використовуватися, якщо параметр не вказано.



    Ось декілька прикладів використання `@RequestParam`:

        1. Отримання параметра запиту зі значенням за замовчуванням:

            @GetMapping("/hello")
            public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
                return "Hello, " + name + "!";
            }

        У цьому прикладі ми використовуємо `@RequestParam` для отримання параметра запиту з ім'ям `name`.
        Якщо параметр не вказано, буде використовуватися значення за замовчуванням "World".


        2. Отримання обов'язкового параметра запиту:

           @GetMapping("/greet")
            public String greet(@RequestParam("name") String name) {
                return "Hello, " + name + "!";
            }

        У цьому прикладі ми використовуємо `@RequestParam` для отримання обов'язкового параметра запиту з ім'ям `name`.
        Якщо параметр не вказано, буде викинуто виняток `MissingServletRequestParameterException`.


        3. Отримання декількох параметрів запиту:

            @GetMapping("/sum")
            public int sum(@RequestParam("a") int a, @RequestParam("b") int b) {
                return a + b;
            }

        У цьому прикладі ми використовуємо `@RequestParam` для отримання двох параметрів запиту з іменами `a` та `b`.
        Значення цих параметрів будуть використовуватися для обчислення суми.
 */