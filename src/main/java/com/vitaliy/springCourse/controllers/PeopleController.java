package com.vitaliy.springCourse.controllers;


import com.vitaliy.springCourse.dao.PersonDAO;
import com.vitaliy.springCourse.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        System.out.println(personDAO.index().toString());
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
            personDAO.save(person);
            return "redirect:/people";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
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
            personDAO.update(id, person);
            return "redirect:/people";
        }
    }

}
