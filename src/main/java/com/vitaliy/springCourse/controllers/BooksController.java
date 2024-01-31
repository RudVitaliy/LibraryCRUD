package com.vitaliy.springCourse.controllers;

import com.vitaliy.springCourse.models.Book;
import com.vitaliy.springCourse.models.Person;
import com.vitaliy.springCourse.services.BooksService;
import com.vitaliy.springCourse.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping("/search")
    public String search(
            Model model,
            @ModelAttribute(name = "title") String startingWith
    ) {
        Book foundBook = booksService.findStartingWith(startingWith);
        model.addAttribute("book", foundBook);
        return "books/search";
    }


    @GetMapping
    public String index(
            Model model,
            @RequestParam(required = false, name = "sort_by_year") Boolean sortByYear,
            @RequestParam(required = false, name = "page") Integer page,
            @RequestParam(required = false, name = "books_per_page") Integer booksPerPage
    ) {
        if (sortByYear != null && sortByYear) {
            if (page != null && page >= 0 && booksPerPage != null && booksPerPage > 0) {
                model.addAttribute(
                        "books", booksService.indexPageable(
                                page,
                                booksPerPage,
                                true
                        )
                );
            } else {
                model.addAttribute("books", booksService.index(true));
            }
        } else {
            if (page != null && page >= 0 && booksPerPage != null && booksPerPage > 0) {
                model.addAttribute(
                        "books", booksService.indexPageable(
                                page,
                                booksPerPage,
                                false
                        )
                );
            } else {
                model.addAttribute("books", booksService.index(false));
            }
        }
        return "/books/index";
    }

    @RequestMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int bookId) {
        model.addAttribute("book", booksService.show(bookId));
        return "/books/edit";
    }

    @GetMapping("/{id}")
    public String show(
            Model model,
            @PathVariable("id") int bookId,
            @ModelAttribute("person") Person person
            ) {
        model.addAttribute("book", booksService.show(bookId));

        Optional<Person> bookOwner = booksService.getBookOwner(bookId);

        if(bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());
        } else {
            model.addAttribute("people", peopleService.index());
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
            booksService.save(book);
            return "redirect:/books";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int bookId) {
        booksService.delete(bookId);
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
            booksService.update(id, book);
            return "redirect:/books";
        }
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(
            @PathVariable("id") int id,
            @ModelAttribute("person") Person selectedPerson
    ) {
        booksService.assignBook(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(
            @PathVariable("id") int id
    ) {
        booksService.releaseBook(id);
        return "redirect:/books/" + id;
    }
}
