package org.baklansbalkan.HeadacheChecker.controllers;

import org.baklansbalkan.HeadacheChecker.dto.JwtResponse;
import org.baklansbalkan.HeadacheChecker.dto.LoginRequest;
import org.baklansbalkan.HeadacheChecker.dto.MessageResponse;
import org.baklansbalkan.HeadacheChecker.dto.SignUpRequest;
import org.baklansbalkan.HeadacheChecker.services.AuthService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public JwtResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public MessageResponse registerUser(@RequestBody SignUpRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }
}
