package com.example.sbdatajpademo.controller;

import com.example.sbdatajpademo.dto.PersonDto;
import com.example.sbdatajpademo.entity.Note;
import com.example.sbdatajpademo.entity.Person;
import com.example.sbdatajpademo.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

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

    @GetMapping("/{personId}/with-notes")
    public Person getPersonByIdFetchNotes(@PathVariable Long personId) {
        return personRepository.findByIdFetchNotes(personId);
    }

    @GetMapping("/by-id-range")
    public List<Person> getPersonsByIdRangeFetchNotes(@RequestParam("personId1") long personId1, @RequestParam("personId2") long personId2) {
        return personRepository.findAllByIdBetweenFetchNotes(personId1, personId2);
    }

    /*
         Якщо ми використовуємо метод `findAllByFirstName` з інтерфейсу `PersonRepository`, то для кожного персону буде
         виконуватись окремий запит до бази даних для отримання його нотаток. Це може призвести до проблем з продуктивністю,
         особливо якщо ми маємо велику кількість персон з багатьма нотатками.
     */
    @GetMapping("/by-name/without-fetch-notes")
    public List<Person> getPersonsByNameWithoutFetchNotes(@RequestParam("firstName") String firstName) {
        return personRepository.findAllByFirstName(firstName);
    }

    /*
        Якщо ми хочемо вигрузити персон з нотатками за один запит, то можна використати `LEFT JOIN FETCH` для з'єднання
        таблиць `Person` та `Note`.
     */
    @GetMapping("/by-name/with-fetch-notes")
    public List<Person> getPersonsByNameWithFetchNotes(@RequestParam("firstName") String firstName) {
        return personRepository.findAllByFirstNameFetchNotes(firstName);
    }

    @GetMapping("/with-notes-count")
    public List<PersonDto> findAllWithNotesCount() {
        return personRepository.findAllWithNotesCount();
    }

//    @PostConstruct
//    public void init() {
//        for (int i = 1; i < 10; i++) {
//            Person person = new Person();
//            person.setFirstName("FirstName" + i);
//            person.setLastName("LastName" + i);
//            person.setAge(i);
//
//            personRepository.save(person);
//        }
//    }

    // create 20 persons and add randomly notes to them
//    @Transactional
//    @PostConstruct
//    public void init() {
//
//        String[] firstNames = {"John", "Jane", "Michael", "Emily", "David"};
//        String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris"};
//
//        Random random = new Random();
//        for (int i = 1; i <= 20; i++) {
//            Person person = new Person();
//            person.setFirstName(firstNames[random.nextInt(firstNames.length)]);
//            person.setLastName(lastNames[random.nextInt(lastNames.length)]);
//            person.setAge(random.nextInt(50) + 18);
//
//            personRepository.save(person);
//        }
//
//        // now let's add randomly to Person with id from 1 to 10
//        List<Person> persons = personRepository.findAllByIdBetween(1L, 10L);
//        for (int i = 1; i <= 50; i++) {
//            Note note = new Note();
//            note.setTitle("Title" + i);
//
//            Person person = persons.get(random.nextInt(persons.size()));
////            note.setPerson(person); // don't need to explicitly set person because we use helper methods
//            person.addNote(note);
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