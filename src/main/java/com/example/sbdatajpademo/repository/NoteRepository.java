package com.example.sbdatajpademo.repository;

import com.example.sbdatajpademo.dto.NoteDto;
import com.example.sbdatajpademo.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    // we can use one of the 4 ways below to use Dto in more efficient way:

    // 1)
//    @Query("select new com.example.sbdatajpademo.dto.NoteDto(n.title, p.firstName, p.lastName) from Note n inner join n.person p")
//    List<NoteDto> findAllDtos();

    // 2)
//    @Query("select new com.example.sbdatajpademo.dto.NoteDto(n.title, n.person.firstName, n.person.lastName) from Note n")
//    List<NoteDto> findAllDtos();

    // 3)
    // without using @Query because Jpa can resolve it by itself, but need to use standard JPA method name
    // otherwise it will not work, or we need to use @Query(...)
    List<NoteDto> findAllBy();

    // 4) using generic = the best choice because you can set needed Dto class in service layer
    <T> List<T> findAllBy(Class<T> type);
}

/*
    Якщо ми говоримо про Spring Data, то Page та Slice є двома різними інтерфейсами, які дозволяють здійснювати пагінацію
    в результатах запитів до бази даних.

    Основна різниця між Page та Slice полягає в тому, як вони обробляють дані під час пагінації.
    Page використовує обмеження (limit) та зсув (offset) для вибірки даних з бази даних, тоді як Slice використовує лише
    обмеження, тобто вибирає певну кількість записів без врахування їх номеру у загальному списку.

    Отже, Page дозволяє повернути всі елементи результату запиту, які попадають у заданий діапазон сторінок, тоді як
    Slice повертає лише ті записи, які відповідають заданому діапазону сторінок. Це означає, що якщо ми хочемо вивести
    загальну кількість сторінок у результаті запиту, то ми повинні використовувати Page. У той же час, якщо нам потрібно
    повернути лише певну кількість записів, яка не залежить від загальної кількості записів, то ми можемо використовувати Slice.

 */