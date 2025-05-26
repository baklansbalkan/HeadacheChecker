package org.baklansbalkan.HeadacheChecker.controllers;

import org.baklansbalkan.HeadacheChecker.dto.UserDTO;
import org.baklansbalkan.HeadacheChecker.services.AdminService;
import org.baklansbalkan.HeadacheChecker.util.EntryNotFoundException;
import org.baklansbalkan.HeadacheChecker.util.HeadacheErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;

    public AdminController(PasswordEncoder passwordEncoder, AdminService adminService) {
        this.passwordEncoder = passwordEncoder;
        this.adminService = adminService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/id/{id}")
    public UserDTO getUserById(@PathVariable int id) {
        return adminService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return adminService.getUserByUsername(username);
    }

    @GetMapping("/email/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        return adminService.getUserByEmail(email);
    }

    @DeleteMapping("/id/{id}")
    public void deleteUser(@PathVariable int id) {
        adminService.deleteUserById(id);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private HeadacheErrorResponse handleHeadacheNotFoundException(EntryNotFoundException exception) {
        return new HeadacheErrorResponse(exception.getMessage());
    }
}
