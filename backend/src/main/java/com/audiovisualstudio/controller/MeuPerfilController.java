
package com.audiovisualstudio.controller;

import com.audiovisualstudio.dto.MeuPerfilRequest;
import com.audiovisualstudio.service.MeuPerfilService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/minha-conta")
public class MeuPerfilController {

    private final MeuPerfilService service;

    public MeuPerfilController(MeuPerfilService service) {
        this.service = service;
    }

    // Consultar meus próprios dados
    @GetMapping
    public ResponseEntity<Map<String, Object>> consultar(
            Principal usuarioLogado) {

        Map<String, Object> perfil =
                service.consultarMeuPerfil(
                        usuarioLogado.getName()
                );

        if (perfil != null) {
            return ResponseEntity.ok(perfil);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // RF35 - Editar meus próprios dados
    @PutMapping
    public ResponseEntity<Map<String, Object>> editar(
            Principal usuarioLogado,
            @RequestBody MeuPerfilRequest dados) {

        try {

            Map<String, Object> resultado =
                    service.editarMeuPerfil(
                            usuarioLogado.getName(),
                            dados
                    );

            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (IllegalArgumentException erro) {

            return ResponseEntity.badRequest().body(
                    Map.of("erro", erro.getMessage())
            );
        }
    }
}
