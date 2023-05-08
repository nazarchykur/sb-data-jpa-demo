package com.example.sbdatajpademo.repository;

import com.example.sbdatajpademo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // all these three methods are equivalent and do the same job
    // 1)
//    List<Person> findByName(String name);

    // 2)
//    @Query("SELECT p FROM Person p WHERE p.fistName = ?1")
//    List<Person> findByName(String name);

    // 3)
    @Query("SELECT p FROM Person p WHERE p.fistName = :fistName")
    List<Person> findByName(@Param("fistName") String fistName);


//    @Query("SELECT p FROM Person p join fetch p.notes WHERE p.id = ?1")
//    Person findByIdFetchNotes(Long id);

    @Query("SELECT p FROM Person p left join fetch p.notes WHERE p.id = ?1")
    Person findByIdFetchNotes(Long id);


    List<Person> findAllByIdBetween(long l, long l1);

    List<Person> findAllByFistName(String fistName);

    /*
        We use `LEFT JOIN FETCH` instead of `JOIN FETCH` in the `findAllByFistNameFetchNotes` query because we want to
        include `Person` entities that do not have any associated `Note` entities in the result set.

        If we used `JOIN FETCH` instead, `Person` entities without any associated `Note` entities would be excluded from
        the result set. This is because `JOIN FETCH` performs an inner join, which only includes rows that have matching
        records in both tables.

        In general, you can use `JOIN FETCH` instead of `LEFT JOIN FETCH` when you know that all records in the left
        table have matching records in the right table. This is often the case when you have defined a foreign key
        constraint with `optional = false`, as you mentioned.

        However, if you are not sure whether all records in the left table have matching records in the right table,
        or if you want to include records from the left table that do not have matching records in the right table, you
        should use `LEFT JOIN FETCH`.

        In summary, you should use `LEFT JOIN FETCH` when you want to include records from the left table that do not
        have matching records in the right table, and `JOIN FETCH` when you know that all records in the left table have
        matching records in the right table.

     */
    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.notes WHERE p.fistName = :fistName")
    List<Person> findAllByFistNameFetchNotes(String fistName);

    /*
        In this example, we are using the `@Query` annotation to specify a JPQL query that selects all `Person` entities
        with an `id` between `id1` and `id2`, and eagerly fetches their `notes` collection using a `LEFT JOIN FETCH` clause.

        Note that we are using the `DISTINCT` keyword in the query to ensure that each `Person` entity is only returned
        once, even if it has multiple `Note` entities in its `notes` collection.

        With this query, you can call the `findAllByIdBetweenFetchNotes` method to retrieve a list of `Person` entities
        with their `notes` collection eagerly fetched.
     */
    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.notes WHERE p.id BETWEEN :id1 AND :id2")
    List<Person> findAllByIdBetweenFetchNotes(@Param("id1") long id1, @Param("id2") long id2);
}

/*
    В Spring Data JPA є можливість використовувати іменовані методи, які автоматично генерують SQL запити на основі
    назви методу та його параметрів. Ось декілька прикладів:

    1. Знайти всіх людей з певним іменем:
        @Query("SELECT p FROM Person p WHERE p.fistName = ?1")
        List<Person> findByName(String fistName);

        Цей метод можна переписати як іменований метод:
            List<Person> findByName(String fistName);

    2. Знайти всіх людей з певним іменем та віком:

        @Query("SELECT p FROM Person p WHERE p.name = ?1 AND p.age = ?2")
        List<Person> findByNameAndAge(String name, int age);

        Цей метод можна переписати як іменований метод:
            List<Person> findByNameAndAge(String name, int age);

    3. Знайти всіх людей з певним іменем та віком, відсортованих за іменем:

        @Query("SELECT p FROM Person p WHERE p.name = ?1 AND p.age = ?2 ORDER BY p.name")
        List<Person> findByNameAndAgeSortedByName(String name, int age);

        Цей метод можна переписати як іменований метод:
            List<Person> findByNameAndAgeOrderByName(String name, int age);

    4. Знайти всіх людей з певним іменем та віком, відсортованих за віком:

        @Query("SELECT p FROM Person p WHERE p.name = ?1 AND p.age = ?2 ORDER BY p.age")
        List<Person> findByNameAndAgeSortedByAge(String name, int age);

        Цей метод можна переписати як іменований метод:
            List<Person> findByNameAndAgeOrderByAge(String name, int age);

----------------------------------------------------------------------------------------------------------

    Ви можете використовувати параметри в анотації `@Query` за допомогою іменованих параметрів або позиційних параметрів.
    Ось приклади обох способів:

    1. Іменовані параметри:

        @Query("SELECT p FROM Person p WHERE p.name = :name AND p.age = :age")
        List<Person> findByNameAndAge(@Param("name") String name, @Param("age") int age);

    У цьому прикладі ми використовуємо іменовані параметри `:name` та `:age` для передачі значень до запиту.
    Ми також використовуємо анотацію `@Param` для вказівки імен параметрів.

    2. Позиційні параметри:

        @Query("SELECT p FROM Person p WHERE p.name = ?1 AND p.age = ?2")
        List<Person> findByNameAndAge(String name, int age);

    У цьому прикладі ми використовуємо позиційні параметри `?1` та `?2` для передачі значень до запиту.
    Перший параметр відповідає `name`, а другий - `age`.

    Обидва способи є еквівалентними і можуть бути використані в залежності від вашого вибору. Іменовані параметри можуть
    бути корисні, якщо ви маєте багато параметрів, оскільки вони дозволяють вам явно вказати, який параметр
    використовується для якої умови. Позиційні параметри можуть бути корисні, якщо ви маєте менше параметрів, оскільки
    вони дозволяють вам просто вказати порядок параметрів.

 */

/*
Query Methods to Retrieve an Entity by Primary Key

    Spring Data Repository allows us to begin our query method names with keywords findBy_, getBy_, readBy_, and queryBy_.
        All these keywords are aliases to each other, and the query methods having these keywords can find a single
        Entity by one or more fields.

        All the query methods in the following snippet are similar. They find a single Entity based on the primary key.

            Optional<Dog> findById(Long id);
            Optional<Dog> readById(Long id);
            Optional<Dog> getById(Long id);
            Optional<Dog> queryById(Long id);



    Query Methods to Retrieve Multiple Entities
        We can find entities by more than one field using the exact keywords and logical operators. Please note that the
        return type of the query methods is a collection, as there could be more than one entity matching the criteria.

            List<Dog> findByAgeAndHeight(Integer age, Double height);
            List<Dog> findByAgeAndNameAndColor(Integer age, String name, String color);
            List<Dog> findByNameOrAge(String name, Integer age);
            List<Dog> findByNameIgnoreCaseAndColor(String name, String color);



    Query Methods to Count Entities
        If a query method name begins with a keyword countBy_, the query returns the count of the Entity beans by one or more fields.

            Integer countByName(String name);
            Integer countByNameAndLastName(String name, String lastName);



    Query Methods to Find the First N Entities
        The Spring Data Repository query methods allow us to retrieve the first N entities that match the criteria.

        Find the first or the topmost Entity that matches the criteria. Both of the following query methods are similar.

            Dog findFirstByName(String name);
            Dog findTopByName(String name);


        Alternatively, we can specify the number of entities we want to retrieve. For example, find the first ten
        entities that match the criteria.

            List<Dog> findTop10ByColor(String color);

        In the real world, we can use this feature and the proper criteria to get meaningful information from the database.
        For example, finding the youngest Dog from the table.

            Dog findTopByOrderByBirthDateDesc();


    Complex Queries with Query Method – Comparison Queries
        It is essential to learn that we can also find entities using more complex search criteria; for example, it
        begins with, contains, greater than, less than, etc.

        Example of finding entities that contain or begin with the given search query string.

            List<Dog> findByNameContaining(String subName);
            List<Dog> findByNameStartingWith(String subName);

        The following snippet contains examples of query methods that use more complex logical search criteria.

            List<Dog> findByHeightLessThan(double height);
            List<Dog> findByAgeLessThanOrHeightGreaterThan(Integer age, double height);
            List<Dog> findByAgeGreaterThanAndAgeLessThan(Integer ageStart, Integer ageEnd);
            List<Dog> findByAgeGreaterThanEqual(Integer age);
            List<Dog> findByDateOfBirthBetween(Date start, Date end);


    Query Methods to Find by Nested Fields
        In an Entity bean, nested fields can represent a Composite Primary Key or Foreign Keys. For example, an Employee
        Has an Address.

        Let’s look at examples of query methods that use nested fields.

            List<Employee> findByNameAndAddressZipCode(Integer zipCode);

        If The Employee entity contains an Address, Spring will evaluate the ‘AddressZipCode‘ expression as ‘address.zipCode‘.
        Alternatively, if the Employee entity has a column ‘addressZipCode‘, Spring will search on that field.


 */


/*
https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords

Appendix C: Repository query keywords
Supported query method subject keywords

Table 8. Query subject keywords
Keyword	Description

+--------------------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------+
| find…By, read…By, get…By,      | General query method returning typically the repository type, a Collection or Streamable subtype or a result wrapper such as Page, GeoResults or any       |
| query…By, search…By, stream…By | other store-specific result wrapper. Can be used as findBy…, findMyDomainTypeBy… or in combination with additional keywords.                               |
+--------------------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------+
| exists…By                      | Exists projection, returning typically a boolean result.                                                                                                   |
+--------------------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------+
| count…By                       | Count projection returning a numeric result.                                                                                                               |
+--------------------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------+
| delete…By, remove…By           | Delete query method returning either no result (void) or the delete count.                                                                                 |
+--------------------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------+
| …First<number>…, …Top<number>… | Limit the query results to the first <number> of results. This keyword can occur in any place of the subject between find (and the other keywords) and by. |
+--------------------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------+
| …Distinct…                     | Use a distinct query to return only unique results. Consult the store-specific documentation whether that feature is supported.                            |
|                                | This keyword can occur in any place of the subject between find (and the other keywords) and by.                                                           |
+--------------------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------+
 */

/*
https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#appendix.query.method.predicate

Supported query method predicate keywords and modifiers

Table 9. Query predicate keywords
Logical keyword	Keyword expressions
+---------------------+------------------------------------------+
| AND                 | And                                      |
+---------------------+------------------------------------------+
| OR                  | Or                                       |
+---------------------+------------------------------------------+
| AFTER               | After, IsAfter                           |
+---------------------+------------------------------------------+
| BEFORE              | Before, IsBefore                         |
+---------------------+------------------------------------------+
| CONTAINING          | Containing, IsContaining, Contains       |
+---------------------+------------------------------------------+
| BETWEEN             | Between, IsBetween                       |
+---------------------+------------------------------------------+
| ENDING_WITH         | EndingWith, IsEndingWith, EndsWith       |
+---------------------+------------------------------------------+
| EXISTS              | Exists                                   |
+---------------------+------------------------------------------+
| FALSE               | False, IsFalse                           |
+---------------------+------------------------------------------+
| GREATER_THAN        | GreaterThan, IsGreaterThan               |
+---------------------+------------------------------------------+
| GREATER_THAN_EQUALS | GreaterThanEqual, IsGreaterThanEqual     |
+---------------------+------------------------------------------+
| IN                  | In, IsIn                                 |
+---------------------+------------------------------------------+
| IS                  | Is, Equals, (or no keyword)              |
+---------------------+------------------------------------------+
| IS_EMPTY            | IsEmpty, Empty                           |
+---------------------+------------------------------------------+
| IS_NOT_EMPTY        | IsNotEmpty, NotEmpty                     |
+---------------------+------------------------------------------+
| IS_NOT_NULL         | NotNull, IsNotNull                       |
+---------------------+------------------------------------------+
| IS_NULL             | Null, IsNull                             |
+---------------------+------------------------------------------+
| LESS_THAN           | LessThan, IsLessThan                     |
+---------------------+------------------------------------------+
| LESS_THAN_EQUAL     | LessThanEqual, IsLessThanEqual           |
+---------------------+------------------------------------------+
| LIKE                | Like, IsLike                             |
+---------------------+------------------------------------------+
| NEAR                | Near, IsNear                             |
+---------------------+------------------------------------------+
| NOT                 | Not, IsNot                               |
+---------------------+------------------------------------------+
| NOT_IN              | NotIn, IsNotIn                           |
+---------------------+------------------------------------------+
| NOT_LIKE            | NotLike, IsNotLike                       |
+---------------------+------------------------------------------+
| REGEX               | Regex, MatchesRegex, Matches             |
+---------------------+------------------------------------------+
| STARTING_WITH       | StartingWith, IsStartingWith, StartsWith |
+---------------------+------------------------------------------+
| TRUE                | True, IsTrue                             |
+---------------------+------------------------------------------+
| WITHIN              | Within, IsWithin                         |
+---------------------+------------------------------------------+

In addition to filter predicates, the following list of modifiers is supported:

Table 10. Query predicate modifier keywords

+--------------------------------+---------------------------------------------------------------------------------------------------------------------+
| IgnoreCase, IgnoringCase       | Used with a predicate keyword for case-insensitive comparison.                                                      |
+--------------------------------+---------------------------------------------------------------------------------------------------------------------+
| AllIgnoreCase, AllIgnoringCase | Ignore case for all suitable properties. Used somewhere in the query method predicate.                              |
+--------------------------------+---------------------------------------------------------------------------------------------------------------------+
| OrderBy…                       | Specify a static sorting order followed by the property path and direction (e. g. OrderByFirstnameAscLastnameDesc). |
+--------------------------------+---------------------------------------------------------------------------------------------------------------------+

*/

/*

Appendix D: Repository query return types
Supported Query Return Types


Table 11. Query return types
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| void                     | Denotes no return value.                                                                                                                                                                                                                                          |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Primitives               | Java primitives.                                                                                                                                                                                                                                                  |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Wrapper types            | Java wrapper types.                                                                                                                                                                                                                                               |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| T                        | A unique entity. Expects the query method to return one result at most. If no result is found, null is returned. More than one result triggers an IncorrectResultSizeDataAccessException.                                                                         |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Iterator<T>              | An Iterator.                                                                                                                                                                                                                                                      |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Collection<T>            | A Collection.                                                                                                                                                                                                                                                     |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| List<T>                  | A List.                                                                                                                                                                                                                                                           |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Optional<T>              | A Java 8 or Guava Optional. Expects the query method to return one result at most. If no result is found, Optional.empty() or Optional.absent() is returned. More than one result triggers an IncorrectResultSizeDataAccessException.                             |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Option<T>                | Either a Scala or Vavr Option type. Semantically the same behavior as Java 8’s Optional, described earlier.                                                                                                                                                       |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Stream<T>                | A Java 8 Stream.                                                                                                                                                                                                                                                  |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Streamable<T>            | A convenience extension of Iterable that directy exposes methods to stream, map and filter results, concatenate them etc.                                                                                                                                         |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Vavr Seq, List, Map, Set | Vavr collection types. See Support for Vavr Collections for details.                                                                                                                                                                                              |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Future<T>                | A Future. Expects a method to be annotated with @Async and requires Spring’s asynchronous method execution capability to be enabled.                                                                                                                              |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| CompletableFuture<T>     | A Java 8 CompletableFuture. Expects a method to be annotated with @Async and requires Spring’s asynchronous method execution capability to be enabled.                                                                                                            |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Slice<T>                 | A sized chunk of data with an indication of whether there is more data available. Requires a Pageable method parameter.                                                                                                                                           |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Page<T>                  | A Slice with additional information, such as the total number of results. Requires a Pageable method parameter.                                                                                                                                                   |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| GeoResult<T>             | A result entry with additional information, such as the distance to a reference location.                                                                                                                                                                         |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| GeoResults<T>            | A list of GeoResult<T> with additional information, such as the average distance to a reference location.                                                                                                                                                         |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| GeoPage<T>               | A Page with GeoResult<T>, such as the average distance to a reference location.                                                                                                                                                                                   |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Mono<T>                  | A Project Reactor Mono emitting zero or one element using reactive repositories. Expects the query method to return one result at most. If no result is found, Mono.empty() is returned. More than one result triggers an IncorrectResultSizeDataAccessException. |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Flux<T>                  | A Project Reactor Flux emitting zero, one, or many elements using reactive repositories. Queries returning Flux can emit also an infinite number of elements.                                                                                                     |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Single<T>                | A RxJava Single emitting a single element using reactive repositories. Expects the query method to return one result at most. If no result is found, Mono.empty() is returned. More than one result triggers an IncorrectResultSizeDataAccessException.           |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Maybe<T>                 | A RxJava Maybe emitting zero or one element using reactive repositories. Expects the query method to return one result at most. If no result is found, Mono.empty() is returned. More than one result triggers an IncorrectResultSizeDataAccessException.         |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Flowable<T>              | A RxJava Flowable emitting zero, one, or many elements using reactive repositories. Queries returning Flowable can emit also an infinite number of elements.                                                                                                      |
+--------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
 */