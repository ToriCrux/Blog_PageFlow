package br.edu.infnet.pageflow.dto;

import br.edu.infnet.pageflow.utils.BlogUserRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignupRequest {

   private String name;

    private String username;

    private String email;

    private String password;

    private BlogUserRoles role;

    public SignupRequest() {
    }

    public SignupRequest(String name, String username, String email, String password, BlogUserRoles role) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BlogUserRoles getRole() {
        return role;
    }

    public void setRole(BlogUserRoles role) {
        this.role = role;
    }
}
