package com.group.libraryapp.domain.book;

import javax.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(nullable = false)
    private String name;

    protected Book() {

    }

    public Book(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s) 이들어왔습니다", name));
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
