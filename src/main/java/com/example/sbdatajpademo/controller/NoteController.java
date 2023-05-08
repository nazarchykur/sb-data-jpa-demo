package com.example.sbdatajpademo.controller;

import com.example.sbdatajpademo.entity.Note;
import com.example.sbdatajpademo.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteRepository noteRepository;

    // for example, we do not need only object Note itself, we need only some fields from this entity and some from another
    //    public List<Note> getAllNotes() {

    @GetMapping
    public Page<Note> findAllNotes(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }

    @GetMapping("/using-slice")
    public Slice<Note> findAllNotesUsingSlice(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }

    // so in this case we can use Dto
//    public List<NoteDto> getAllNotes() {
//        // title, personFirstName, personLastName
//    }
}

/*
    Патерн DTO (Data Transfer Object) використовується для передачі даних між компонентами програми, зазвичай між
    бізнес-логікою та її представленням (наприклад, між контролерами та сервісами). Ідея полягає в тому, щоб мати окремий
    об'єкт, який містить лише необхідні для передачі дані та їх формат.

    Щодо використання патерну DTO в Spring Boot, основна рекомендація полягає в тому, щоб не зловживати його використанням.
    Це означає, що DTO повинні використовуватись лише тоді, коли є потреба в передачі даних між різними компонентами, а
    не в кожному класі та методі. Крім того, бажано використовувати автоматичну конвертацію об'єктів (наприклад, за
    допомогою бібліотеки ModelMapper), щоб уникнути дублювання коду та забезпечити максимальну гнучкість.

    Основні переваги використання паттерна DTO в Spring Boot:

        1. Зменшення кількості даних, які передаються між частинами програми.
        2. Зменшення навантаження на мережу.
        3. Зменшення кількості запитів до бази даних.
        4. Збереження бізнес-логіки в сервісах, а не в контролерах.

    Основні правила використання паттерна DTO в Spring Boot:

        1. DTO повинен містити тільки ті поля, які потрібні для передачі даних.
        2. DTO не повинен містити жодної бізнес-логіки.
        3. DTO повинен бути імутабельним (тобто, його поля не повинні змінюватись після створення об'єкта).
        4. DTO повинен мати конструктор без параметрів та конструктор з параметрами для ініціалізації полів.
        5. DTO повинен мати методи доступу до полів (гетери та сетери).

    Отже, паттерн DTO дозволяє зменшити кількість даних, які передаються між частинами програми, і зменшити навантаження
    на мережу. У Spring Boot паттерн DTO часто використовується для передачі даних між контролерами та сервісами. При
    використанні паттерна DTO в Spring Boot потрібно дотримуватись певних правил, щоб забезпечити правильну роботу програми.

 */

/*
    Pageable - це інтерфейс Spring Framework, який дозволяє здійснювати посторінкову вибірку даних з бази даних.
    Цей інтерфейс містить інформацію про номер сторінки, розмір сторінки та критерії сортування даних.

    Pageable може використовуватися в разі, коли ви хочете повернути обмежену кількість даних з БД та поділити їх на
    сторінки для легшого відображення в веб-додатку. Наприклад, коли ви відображаєте список товарів на сторінці з можливістю
    переходу на наступну або попередню сторінку, можна використовувати Pageable для налаштування розміру сторінки та вибору
    поточної сторінки.

    Основні переваги використання Pageable в Spring Boot полягають в тому, що це дозволяє:

        > Зменшити навантаження на сервер, оскільки не потрібно повертати всі дані одразу, а лише ту частину, яка потрібна на даному етапі.
        > Оптимізувати роботу з БД, оскільки можна обмежити кількість запитів до БД, які повертають дані.
        > Спростити розробку веб-додатків, оскільки Pageable забезпечує готові методи для роботи з пагінацією.

    Щодо найкращих практик використання Pageable в Spring Boot, основні рекомендації полягають в тому, щоб:
        > Використовувати Pageable тільки тоді, коли ви дійсно потребуєте пагінацію даних.
        > Не використовувати Pageable в великих таблицях або з багатьма з'єднаннями, оскільки це може призвести до погіршення продуктивності.
        > Не використовувати Pageable для відображення всіх даних у вигляді таблиці без пагінації.
        > Встановлювати розмір сторінки як вхідний параметр, щоб користувач міг налаштувати його за своїми потребами.
        > Перевіряти правильність вхідних параметрів Pageable, щоб запобігти виконанню зайвих запитів до БД.
        > Використовувати анотацію @PageableDefault для встановлення значень за замовчуванням розміру сторінки та сортування.

    Наприклад, для використання Pageable в Spring Boot можна написати наступний код:
        @Service
        public class ProductService {

            @Autowired
            private ProductRepository productRepository;

            public Page<ProductDto> getAllProducts(int page, int size, String[] sort) {
                Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
                Page<Product> products = productRepository.findAll(pageable);
                Page<ProductDto> productDtos = products.map(productMapper::toDto);
                return productDtos;
            }
        }

        @GetMapping("/products")
        public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "id,desc") String[] sort) {
            Page<ProductDto> productDtos = productService.getAllProducts(page, size, sort);
            if (productDtos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productDtos, HttpStatus.OK);
        }

    У цьому прикладі код у контролері містить лише обробку запиту та передачу параметрів до сервісу.
    У свою чергу, сервіс містить логіку пагінації та виконує запит до БД.
    Такий підхід дозволить зберегти час на розробці та зробити код більш чистим та зрозумілим.

 */

/*

GET http://localhost:9001/notes


###
GET http://localhost:9001/notes?size=10&page=2


###
GET http://localhost:9001/notes?size=3&page=13&sort=title,desc

###
GET http://localhost:9001/notes/using-slice
//--//--// the same as above when we use page
 */