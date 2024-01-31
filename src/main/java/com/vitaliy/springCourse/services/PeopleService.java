package com.vitaliy.springCourse.services;

import com.vitaliy.springCourse.models.Book;
import com.vitaliy.springCourse.models.Person;
import com.vitaliy.springCourse.repositories.BooksRepository;
import com.vitaliy.springCourse.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    private final BooksService booksService;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository, BooksService booksService) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
        this.booksService = booksService;
    }

    public List<Person> index() {
        return peopleRepository.findAll();
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }


    public Person show(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName).stream().findAny();
    }

    public List<Book> getBooksByPersonId(int personId) {
        List<Book> booksByOwner = booksRepository.findByOwner(show(personId));
        booksByOwner.forEach(booksService::isBookReturnedOnTime);
        return booksByOwner;
    }
}
