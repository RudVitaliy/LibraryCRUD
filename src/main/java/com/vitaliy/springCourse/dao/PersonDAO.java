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
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public void save(Person person) {
        jdbcTemplate.update(
                "INSERT INTO Person(fullName, yearOfBirth) VALUES(?, ?)",
                person.getFullName(),
                person.getYearOfBirth());
    }


    public Person show(int id) {
        return jdbcTemplate.query(
                "SELECT * FROM Person WHERE id=?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)
        ).stream().findAny().orElse(null);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    public void update(int id, Person person) {
        jdbcTemplate.update(
                "UPDATE Person SET fullName=?, yearOfBirth=? WHERE id=?",
                person.getFullName(),
                person.getYearOfBirth(),
                id
        );
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return jdbcTemplate.query(
                "SELECT * FROM Person WHERE fullName=?",
                new Object[]{fullName},
                new BeanPropertyRowMapper<>(Person.class)
        ).stream().findAny();
    }

    public List<Book> getBooksByPersonId(int personId) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id=?",
                new Object[]{personId},
                new BeanPropertyRowMapper<>(Book.class)
        );
    }
}
