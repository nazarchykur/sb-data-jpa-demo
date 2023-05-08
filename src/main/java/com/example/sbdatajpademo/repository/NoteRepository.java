package com.example.sbdatajpademo.repository;

import com.example.sbdatajpademo.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}