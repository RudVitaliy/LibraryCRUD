package com.vitaliy.springCourse.controllers;


import com.vitaliy.springCourse.models.Person;
import com.vitaliy.springCourse.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", peopleService.index());
        return "people/index";
    }

    @RequestMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "/people/new";
    }

    @PostMapping
    public String create(
            @ModelAttribute("person") @Valid Person person,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        } else {
            peopleService.save(person);
            return "redirect:/people";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.show(id));
        return "people/edit";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        System.out.println("debug");
        model.addAttribute("person", peopleService.show(id));
        model.addAttribute("books", peopleService.getBooksByPersonId(id));
        return "people/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

    @PatchMapping("/{id}")
    public String update (
            @ModelAttribute("person") @Valid Person person,
            BindingResult bindingResult,
            @PathVariable("id") int id
    ) {
        if (bindingResult.hasErrors()) {
            return "people/edit";
        } else {
            peopleService.update(id, person);
            return "redirect:/people";
        }
    }

}
