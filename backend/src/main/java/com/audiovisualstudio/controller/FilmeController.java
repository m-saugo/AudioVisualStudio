
package com.audiovisualstudio.controller;

import com.audiovisualstudio.model.Filme;
import com.audiovisualstudio.model.Ator;
import com.audiovisualstudio.model.Diretor;
import com.audiovisualstudio.model.Roteirista;

import com.audiovisualstudio.service.FilmeService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/filmes")
public class FilmeController {

    private final FilmeService service;

    public FilmeController(FilmeService service) {
        this.service = service;
    }

    // =====================================
    // CRUD DE FILMES - RF13 A RF16
    // =====================================

    // RF13 - Cadastrar filme
    @PostMapping
    public Filme cadastrar(@RequestBody Filme filme) {
        return service.cadastrar(filme);
    }

    // RF15 - Consultar todos
    @GetMapping
    public List<Filme> consultarTodos() {
        return service.consultarTodos();
    }

    // RF15 - Consultar por codigo
    @GetMapping("/{codigo}")
    public ResponseEntity<Filme> consultarPorId(
            @PathVariable Long codigo) {

        Optional<Filme> filme = service.consultarPorId(codigo);

        if (filme.isPresent()) {
            return ResponseEntity.ok(filme.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF14 - Editar filme
    @PutMapping("/{codigo}")
    public ResponseEntity<Filme> editar(
            @PathVariable Long codigo,
            @RequestBody Filme dadosAtualizados) {

        Filme filme = service.editar(codigo, dadosAtualizados);

        if (filme != null) {
            return ResponseEntity.ok(filme);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF16 - Excluir filme
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long codigo) {

        boolean excluido = service.excluir(codigo);

        if (excluido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // =====================================
    // ATORES
    // =====================================

    // RF17 - Associar ator
    @PostMapping("/{codigo}/atores/{idAtor}")
    public ResponseEntity<Void> associarAtor(
            @PathVariable Long codigo,
            @PathVariable Long idAtor) {

        boolean associado = service.associarAtor(codigo, idAtor);

        if (associado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF20 - Remover ator
    @DeleteMapping("/{codigo}/atores/{idAtor}")
    public ResponseEntity<Void> removerAtor(
            @PathVariable Long codigo,
            @PathVariable Long idAtor) {

        boolean removido = service.removerAtor(codigo, idAtor);

        if (removido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF23 - Consultar elenco
    @GetMapping("/{codigo}/atores")
    public ResponseEntity<List<Ator>> consultarElenco(
            @PathVariable Long codigo) {

        List<Ator> elenco = service.consultarElenco(codigo);

        if (elenco != null) {
            return ResponseEntity.ok(elenco);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // =====================================
    // DIRETORES
    // =====================================

    // RF18 - Associar diretor
    @PostMapping("/{codigo}/diretores/{idDiretor}")
    public ResponseEntity<Void> associarDiretor(
            @PathVariable Long codigo,
            @PathVariable Long idDiretor) {

        boolean associado =
                service.associarDiretor(codigo, idDiretor);

        if (associado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF21 - Remover diretor
    @DeleteMapping("/{codigo}/diretores/{idDiretor}")
    public ResponseEntity<Void> removerDiretor(
            @PathVariable Long codigo,
            @PathVariable Long idDiretor) {

        boolean removido =
                service.removerDiretor(codigo, idDiretor);

        if (removido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF24 - Consultar diretores do filme
    @GetMapping("/{codigo}/diretores")
    public ResponseEntity<List<Diretor>> consultarDiretores(
            @PathVariable Long codigo) {

        List<Diretor> diretores =
                service.consultarDiretores(codigo);

        if (diretores != null) {
            return ResponseEntity.ok(diretores);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // =====================================
    // ROTEIRISTAS
    // =====================================

    // RF19 - Associar roteirista
    @PostMapping("/{codigo}/roteiristas/{idRoteirista}")
    public ResponseEntity<Void> associarRoteirista(
            @PathVariable Long codigo,
            @PathVariable Long idRoteirista) {

        boolean associado =
                service.associarRoteirista(codigo, idRoteirista);

        if (associado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF22 - Remover roteirista
    @DeleteMapping("/{codigo}/roteiristas/{idRoteirista}")
    public ResponseEntity<Void> removerRoteirista(
            @PathVariable Long codigo,
            @PathVariable Long idRoteirista) {

        boolean removido =
                service.removerRoteirista(codigo, idRoteirista);

        if (removido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF24 - Consultar roteiristas do filme
    @GetMapping("/{codigo}/roteiristas")
    public ResponseEntity<List<Roteirista>> consultarRoteiristas(
            @PathVariable Long codigo) {

        List<Roteirista> roteiristas =
                service.consultarRoteiristas(codigo);

        if (roteiristas != null) {
            return ResponseEntity.ok(roteiristas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF24 - Consultar equipe completa do filme
    @GetMapping("/{codigo}/equipe")
    public ResponseEntity<Map<String, Object>> consultarEquipe(
            @PathVariable Long codigo) {

        List<Diretor> diretores =
                service.consultarDiretores(codigo);

        List<Roteirista> roteiristas =
                service.consultarRoteiristas(codigo);

        if (diretores != null && roteiristas != null) {

            Map<String, Object> equipe = new HashMap<>();

            equipe.put("diretores", diretores);
            equipe.put("roteiristas", roteiristas);

            return ResponseEntity.ok(equipe);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // =====================================
    // CONSULTAS - RF26 A RF29
    // =====================================

    // RF26 - Consultar todos os profissionais de um filme
    @GetMapping("/{codigo}/profissionais")
    public ResponseEntity<Map<String, Object>> consultarProfissionais(
            @PathVariable Long codigo) {

        Map<String, Object> profissionais =
                service.consultarProfissionais(codigo);

        if (profissionais != null) {
            return ResponseEntity.ok(profissionais);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF27 - Consultar filmes de um ator
    @GetMapping("/por-ator/{idAtor}")
    public ResponseEntity<List<Map<String, Object>>> consultarFilmesDoAtor(
            @PathVariable Long idAtor) {

        List<Map<String, Object>> filmes =
                service.consultarFilmesDoAtor(idAtor);

        if (filmes != null) {
            return ResponseEntity.ok(filmes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF28 - Consultar filmes de um diretor
    @GetMapping("/por-diretor/{idDiretor}")
    public ResponseEntity<List<Map<String, Object>>> consultarFilmesDoDiretor(
            @PathVariable Long idDiretor) {

        List<Map<String, Object>> filmes =
                service.consultarFilmesDoDiretor(idDiretor);

        if (filmes != null) {
            return ResponseEntity.ok(filmes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF29 - Consultar filmes de um roteirista
    @GetMapping("/por-roteirista/{idRoteirista}")
    public ResponseEntity<List<Map<String, Object>>> consultarFilmesDoRoteirista(
            @PathVariable Long idRoteirista) {

        List<Map<String, Object>> filmes =
                service.consultarFilmesDoRoteirista(idRoteirista);

        if (filmes != null) {
            return ResponseEntity.ok(filmes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
