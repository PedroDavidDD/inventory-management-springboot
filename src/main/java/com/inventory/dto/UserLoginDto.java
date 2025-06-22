package com.inventory.dto;

import jakarta.validation.constraints.NotBlank;

public class UserLoginDto {
    @NotBlank(message = "Usuario es requerido")
    private String username;

    @NotBlank(message = "Contraseña es requerida")
    private String password;

    // Constructors
    public UserLoginDto() {}

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}