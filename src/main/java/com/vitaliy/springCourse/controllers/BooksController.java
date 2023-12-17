package com.vitaliy.springCourse.controllers;

import com.vitaliy.springCourse.dao.BookDAO;
import com.vitaliy.springCourse.dao.PersonDAO;
import com.vitaliy.springCourse.models.Book;
import com.vitaliy.springCourse.models.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());
        return "/books/index";
    }

    @RequestMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int bookId) {
        model.addAttribute("book", bookDAO.show(bookId));
        return "/books/edit";
    }

    @GetMapping("/{id}")
    public String show(
            Model model,
            @PathVariable("id") int bookId,
            @ModelAttribute("person") Person person
            ) {
        model.addAttribute("book", bookDAO.show(bookId));

        Optional<Person> bookOwner = bookDAO.getBookOwner(bookId);

        if(bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());
        } else {
            model.addAttribute("people", personDAO.index());
        }

        return "/books/show";
    }

    @PostMapping
    public String create(
            @ModelAttribute("book") Book book,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "/books/new";
        } else  {
            bookDAO.save(book);
            return "redirect:/books";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int bookId) {
        bookDAO.delete(bookId);
        return "redirect:/books";
    }

    @PatchMapping("/{id}")
    public String update (
            @ModelAttribute("book") @Valid Book book,
            BindingResult bindingResult,
            @PathVariable("id") int id
    ) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        } else {
            bookDAO.update(id, book);
            return "redirect:/books";
        }
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(
            @PathVariable("id") int id,
            @ModelAttribute("person") Person selectedPerson
    ) {
        bookDAO.assignBook(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(
            @PathVariable("id") int id
    ) {
        bookDAO.releaseBook(id);
        return "redirect:/books/" + id;
    }
}
