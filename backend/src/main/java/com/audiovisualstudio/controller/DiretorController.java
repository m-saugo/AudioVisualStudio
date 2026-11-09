
package com.audiovisualstudio.controller;

import com.audiovisualstudio.model.Diretor;
import com.audiovisualstudio.service.DiretorService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diretores")
public class DiretorController {

    private final DiretorService service;

    // Construtor
    public DiretorController(DiretorService service) {
        this.service = service;
    }

    // RF05 - Cadastrar diretor
    @PostMapping
    public Diretor cadastrar(@RequestBody Diretor diretor) {
        return service.cadastrar(diretor);
    }

    // RF07 - Consultar todos os diretores
    @GetMapping
    public List<Diretor> consultarTodos() {
        return service.consultarTodos();
    }

    // RF07 - Consultar diretor pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Diretor> consultarPorId(
            @PathVariable Long id) {

        Optional<Diretor> diretor =
                service.consultarPorId(id);

        if (diretor.isPresent()) {
            return ResponseEntity.ok(diretor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF06 - Editar diretor
    @PutMapping("/{id}")
    public ResponseEntity<Diretor> editar(
            @PathVariable Long id,
            @RequestBody Diretor dadosAtualizados) {

        Diretor diretor = service.editar(id, dadosAtualizados);

        if (diretor != null) {
            return ResponseEntity.ok(diretor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF08 - Excluir diretor
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
