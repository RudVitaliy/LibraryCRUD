package com.vitaliy.springCourse.dao;

import com.vitaliy.springCourse.models.Book;
import com.vitaliy.springCourse.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public void save(Book book) {
        jdbcTemplate.update(
                "INSERT INTO Book(bookTitle, author, yearOfPublication) VALUES(?, ?, ?)",
                book.getBookTitle(),
                book.getAuthor(),
                book.getYearOfPublication()
        );
    }

    public Book show(int bookId) {
        return jdbcTemplate.query(
                "SELECT * FROM Book WHERE id=?",
                new Object[]{bookId},
                new BeanPropertyRowMapper<>(Book.class)
        ).stream().findAny().orElse(null);
    }

    public void delete(int bookId) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", bookId);
    }

    public void update(int id, Book book) {
        jdbcTemplate.update(
                "UPDATE Book SET bookTitle=?, yearOfPublication=?, author=? WHERE id=?",
                book.getBookTitle(),
                book.getYearOfPublication(),
                book.getAuthor(),
                id
        );
    }



    public void assignBook(int bookId, Person person) {
        jdbcTemplate.update(
                "UPDATE Book SET person_id=? WHERE id=?",
                person.getId(),
                bookId
        );
    }

    public void releaseBook(int id) {
        jdbcTemplate.update(
            "UPDATE Book SET person_id=NULL WHERE id=?",
                id
        );
    }

    public Optional<Person> getBookOwner(int bookId) {
        return jdbcTemplate.query(
            "SELECT Person.* FROM Book JOIN Person ON Book.person_id = Person.id WHERE Book.id=?",
                new Object[]{bookId},
                new BeanPropertyRowMapper<>(Person.class)
        ).stream().findAny();
    }
}
