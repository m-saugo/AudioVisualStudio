
package com.audiovisualstudio.controller;

import com.audiovisualstudio.model.Ator;
import com.audiovisualstudio.service.AtorService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/atores")
public class AtorController {

    private final AtorService service;

    // Construtor - Injeção de dependência
    public AtorController(AtorService service) {
        this.service = service;
    }

    // RF01 - Cadastrar ator
    @PostMapping
    public Ator cadastrar(@RequestBody Ator ator) {
        return service.cadastrar(ator);
    }

    // RF03 - Consultar todos os atores
    @GetMapping
    public List<Ator> consultarTodos() {
        return service.consultarTodos();
    }

    // RF03 - Consultar ator pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Ator> consultarPorId(
            @PathVariable Long id) {

        Optional<Ator> ator = service.consultarPorId(id);

        if (ator.isPresent()) {
            return ResponseEntity.ok(ator.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF02 - Editar ator
    @PutMapping("/{id}")
    public ResponseEntity<Ator> editar(
            @PathVariable Long id,
            @RequestBody Ator dadosAtualizados) {

        Ator ator = service.editar(id, dadosAtualizados);

        if (ator != null) {
            return ResponseEntity.ok(ator);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF04 - Excluir ator
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        boolean excluido = service.excluir(id);

        if (excluido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
