package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;


    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUsersTable(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/adduser")
    @PreAuthorize("hasRole('ADMIN')")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "adduser";
    }

    @PostMapping("/adduser")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveUser(@ModelAttribute("user") @Validated User user,
                           @RequestParam("roleId") List<Long> roleId)
    {
        List<Role> roles = new ArrayList<>(roleService.getRolesById(roleId));
        userService.saveUser(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/updateUserForm")
    public String updateUserForm(@RequestParam("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "updateuser";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") @Validated User user, @RequestParam List<Long> roleId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "updateuser";
        }
        List<Role> roles = roleService.getRolesById(roleId);
        userService.updateUser(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@ModelAttribute("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
