package com.inventory.controller;

import com.inventory.dto.UserLoginDto;
import com.inventory.dto.UserRegistrationDto;
import com.inventory.entity.Role;
import com.inventory.entity.User;
import com.inventory.repository.RoleRepository;
import com.inventory.repository.UserRepository;
import com.inventory.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            User user = userService.registerUser(registrationDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            response.put("userId", user.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto loginDto) {
        Optional<User> userOpt = userService.findByUsername(loginDto.getUsername());
        
        if (userOpt.isPresent() && passwordEncoder.matches(loginDto.getPassword(), userOpt.get().getPasswordHash())) {
            User user = userOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "role", user.getRoles()
            ));
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Credenciales inválidas");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/assign-role")
    public ResponseEntity<?> assignRole(@RequestParam String username,
                                        @RequestParam String roleName,
                                        @RequestParam String currentUsername) {
        Optional<User> currentUserOpt = userService.findByUsername(currentUsername);
        Optional<User> targetUserOpt = userService.findByUsername(username);

        if (currentUserOpt.isEmpty() || !currentUserOpt.get().hasRole("admin")) {
            throw new RuntimeException("Solo el admin puede asignar roles");
        }

        if (targetUserOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isEmpty()) {
            throw new RuntimeException("Rol no válido");
        }

        User targetUser = targetUserOpt.get();
        Set<Role> roles = targetUser.getRoles();
        roles.clear(); // Opcional: limpiar roles anteriores
        roles.add(roleOpt.get());
        targetUser.setRoles(roles);

        userRepository.save(targetUser);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Rol asignado exitosamente");

        return ResponseEntity.ok(response);
    }

}