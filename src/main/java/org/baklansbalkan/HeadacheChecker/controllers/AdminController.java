package org.baklansbalkan.HeadacheChecker.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.baklansbalkan.HeadacheChecker.dto.UserDTO;
import org.baklansbalkan.HeadacheChecker.services.AdminService;
import org.baklansbalkan.HeadacheChecker.util.EntryNotFoundException;
import org.baklansbalkan.HeadacheChecker.util.HeadacheErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Get information about users", description = "Returns the full list of users")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return adminService.getAllUsers();
    }

    @Operation(summary = "Get information about one user by id", description = "Returns the user with current id")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/id/{id}")
    public UserDTO getUserById(@PathVariable int id) {
        return adminService.getUserById(id);
    }

    @Operation(summary = "Get information about one user by username", description = "Returns the user with current username")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return adminService.getUserByUsername(username);
    }

    @Operation(summary = "Get information about one user by email", description = "Returns the user with current email")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/email/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        return adminService.getUserByEmail(email);
    }

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "200", description = "Success")
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
