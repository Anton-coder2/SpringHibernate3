package ru.alishev.springcourse.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.dao.UserDao;
import ru.alishev.springcourse.models.User;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

/**
 * @author Neil Alishev
 */
@Controller
@RequestMapping("/people")
public class PeopleController {

    private final UserDao userDao;

    @Autowired
    public PeopleController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping()
    public String index(Model model) {
        List<User> userList = userDao.index();
        userList.sort(Comparator.comparing(x -> x.getId()));
        model.addAttribute("people", userList);
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userDao.show(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("user") User user) {
        return "people/new";
    }

    @PostMapping()
    public String create(User user) throws SQLException {
        userDao.save(user);
        return "redirect:/people";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", userDao.index().stream().filter(x -> x.getId()==id).findFirst().get());
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(User user, @PathVariable("id") int id) {
        user.setId(id);
        userDao.update(user);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userDao.delete(userDao.index().stream().filter(x -> x.getId()==id).findFirst().get());
        return "redirect:/people";
    }
}
