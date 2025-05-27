package org.baklansbalkan.HeadacheChecker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login request", example = """
        {
          "username": "testuser",
          "password": "testpassword"
        }""")
public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
