package com.inventory.controller;

import com.inventory.entity.Tag;
import com.inventory.repository.TagRepository;
import com.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductService productService;

    // Listar todos los tags
    @GetMapping
    public ResponseEntity<Set<Tag>> getAllTags() {
        return ResponseEntity.ok(Set.of(tagRepository.findAll().toArray(Tag[]::new)));
    }

    // Registrar un nuevo tag manualmente
    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        if (tag.getId() == null || tag.getName() == null || tag.getName().trim().isEmpty()) {
            throw new RuntimeException("Nombre de etiqueta inválido");
        }
        tagRepository.save(tag);
        return ResponseEntity.ok(Map.of("success", true, "message", "Etiqueta creada exitosamente"));
    }

    // Editar un tag
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable String id, @RequestBody Tag updatedTag) {
        return tagRepository.findById(id)
                .map(tag -> {
                    tag.setName(updatedTag.getName());
                    tagRepository.save(tag);

                    return ResponseEntity.ok(Map.of("success", true, "message", "Etiqueta actualizada"));
                })
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));
    }

    // Eliminar un tag
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable String id) {
        try {
            productService.deleteTagById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Etiqueta eliminada correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}