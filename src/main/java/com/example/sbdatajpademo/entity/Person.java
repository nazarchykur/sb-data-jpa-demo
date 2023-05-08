package com.example.sbdatajpademo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private Integer age;

    // added EAGER only for init method in controller to randomly add Note to Person with id from 1 to 10
    // only one time to add data to DB
    @JsonManagedReference // only for test purpose when we work in controller with entities
//    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();

    public void addNote(Note note) {
        note.setPerson(this);
        this.notes.add(note);
    }

    public void removeNote(Note note) {
        this.notes.remove(note);
        note.setPerson(null);
    }

}