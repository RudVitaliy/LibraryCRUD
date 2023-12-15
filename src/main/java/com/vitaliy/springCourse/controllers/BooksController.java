package com.vitaliy.springCourse.controllers;

import com.vitaliy.springCourse.dao.BookDAO;
import com.vitaliy.springCourse.models.Book;
import com.vitaliy.springCourse.models.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;

    public BooksController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
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
    public String show(Model model, @PathVariable("id") int bookId) {
        model.addAttribute("book", bookDAO.show(bookId));
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
}
