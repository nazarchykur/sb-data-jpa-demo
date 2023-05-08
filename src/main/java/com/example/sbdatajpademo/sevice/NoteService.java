package com.example.sbdatajpademo.sevice;

import com.example.sbdatajpademo.dto.NoteDto;
import com.example.sbdatajpademo.dto.NoteTitleDto;
import com.example.sbdatajpademo.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    /*
      теоретично можна передавати сюди як параметр Pageable потім перемаплювати,
      але тут є інше але, яке повязане з вигрузкою даних з БД та опрацюванні на стороні java:
          - 1) вигружаються в ріквесті з БД до java всі колонки
                (тобто Hibernate загрузить всі ці ентіті з усіма полями собі у сесію)
                по факту нам не потрібні всі ці дані з ентіті бо ми їх зрузу тут перемаплюємо у ДТО
          - 2) немає JOIN, а для потрібних даних йде додатковий запит у БД
          - 3) можна поставити @Transactional(readOnly = true) щоб Hibernate не робив додаткових дій для dirty checking
                але тут всеодно є неефективно те, що Hibernate загрузить всі ці ентіті з усіма полями

       тобто так робити НЕ правильно і не ефективно
     */
    @Transactional(readOnly = true)
    public List<NoteDto> getAllNotes() {
        return noteRepository.findAll().stream()
                .map(n -> new NoteDto(n.getTitle(), n.getPerson().getFirstName(), n.getPerson().getLastName()))
                .toList();
    }

    /*
        це накращий спосіб який можна використовувати, щоб отримати дані з БД, які нам потрібні тільки для читання і ще
        якщо там є агрегування даних (дані з кількох таблиць, а саме тільки потрібні поля з кожної талиці)
     */
//    @Transactional(readOnly = true)
//    public List<NoteDto> getAllNotesUsingQueryWithDto() {
//        return noteRepository.findAllDtos();
//    }

    @Transactional(readOnly = true)
    public List<NoteDto> getAllNotesUsingQueryWithDto() {
        return noteRepository.findAllBy();
    }

    @Transactional(readOnly = true)
    public List<NoteDto> getAllNotesUsingQueryWithGenericDto() {
        return noteRepository.findAllBy(NoteDto.class);
    }

    @Transactional(readOnly = true)
    public List<NoteTitleDto> getAllNotesDtoOnlyTitle() {
        return noteRepository.findAllBy(NoteTitleDto.class);
    }
}
