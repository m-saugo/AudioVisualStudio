
package com.audiovisualstudio.controller;

import com.audiovisualstudio.model.Roteirista;
import com.audiovisualstudio.service.RoteiristaService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roteiristas")
public class RoteiristaController {

    private final RoteiristaService service;

    public RoteiristaController(RoteiristaService service) {
        this.service = service;
    }

    // RF09 - Cadastrar roteirista
    @PostMapping
    public Roteirista cadastrar(
            @RequestBody Roteirista roteirista) {

        return service.cadastrar(roteirista);
    }

    // RF11 - Consultar todos os roteiristas
    @GetMapping
    public List<Roteirista> consultarTodos() {
        return service.consultarTodos();
    }

    // RF11 - Consultar roteirista pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Roteirista> consultarPorId(
            @PathVariable Long id) {

        Optional<Roteirista> roteirista =
                service.consultarPorId(id);

        if (roteirista.isPresent()) {
            return ResponseEntity.ok(roteirista.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF10 - Editar roteirista
    @PutMapping("/{id}")
    public ResponseEntity<Roteirista> editar(
            @PathVariable Long id,
            @RequestBody Roteirista dadosAtualizados) {

        Roteirista roteirista =
                service.editar(id, dadosAtualizados);

        if (roteirista != null) {
            return ResponseEntity.ok(roteirista);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF12 - Excluir roteirista
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
