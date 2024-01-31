package com.vitaliy.springCourse.services;

import com.vitaliy.springCourse.models.Book;
import com.vitaliy.springCourse.models.Person;
import com.vitaliy.springCourse.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    public void isBookReturnedOnTime(Book book) {
        if (book.getTakenAt() == null) {
            book.setReturnedOnTime(true);
        } else {
            LocalDate bookTakenLocalDate = Instant.ofEpochMilli(book.getTakenAt().getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate currentDate = LocalDate.now();
            long daysDifference = ChronoUnit.DAYS.between(bookTakenLocalDate, currentDate);
            book.setReturnedOnTime(daysDifference <= 10);
        }
    }

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> index(boolean sorted) {
        if(sorted) {
            return booksRepository.findAll(Sort.by("yearOfPublication"));
        } else {
            return booksRepository.findAll();
        }
    }

    public List<Book> indexPageable(int page, int booksPerPage, boolean sorted) {
        if (sorted) {
            return booksRepository.findAll(
                    PageRequest.of(page, booksPerPage, Sort.by(Sort.Direction.DESC,"yearOfPublication"))
            ).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public Book findStartingWith(String startingWith) {
        return booksRepository.findByBookTitleStartingWith(startingWith).stream().findAny().orElse(null);
    }


    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    public Book show(int bookId) {
        return booksRepository.findById(bookId).orElse(null);
    }

    @Transactional
    public void delete(int bookId) {
        booksRepository.deleteById(bookId);
    }

    @Transactional
    public void update(int id, Book book) {
        Book bookToBeUpdated = booksRepository.findById(id).get();
        book.setId(id);
        book.setOwner(bookToBeUpdated.getOwner());
        booksRepository.save(book);
    }

    @Transactional
    public void assignBook(int bookId, Person person) {
        Book bookToAssign = show(bookId);
        bookToAssign.setOwner(person);
        bookToAssign.setTakenAt(new Date());
        update(bookId, bookToAssign);
    }

    @Transactional
    public void releaseBook(int id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                    update(id, book);
                });
    }

    public Optional<Person> getBookOwner(int bookId) {
        Book book = show(bookId);
        return Optional.ofNullable(book.getOwner());
    }

}
