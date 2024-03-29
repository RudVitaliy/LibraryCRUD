package com.vitaliy.springCourse.repositories;

import com.vitaliy.springCourse.models.Book;
import com.vitaliy.springCourse.models.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);

    List<Book> findByBookTitleStartingWith(String startingWith);

}
