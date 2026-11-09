
package com.audiovisualstudio.controller;

import com.audiovisualstudio.dto.CadastroRequest;
import com.audiovisualstudio.model.Usuario;
import com.audiovisualstudio.service.CadastroService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/cadastro")
public class CadastroController {

    private final CadastroService service;

    public CadastroController(CadastroService service) {
        this.service = service;
    }

    // Cadastro proprio de usuarios
    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrar(
            @RequestBody CadastroRequest dados) {

        try {

            Usuario usuario = service.cadastrar(dados);

            Map<String, Object> resposta = new LinkedHashMap<>();

            resposta.put(
                    "mensagem",
                    "Cadastro realizado com sucesso!"
            );

            resposta.put("idUsuario", usuario.getId());
            resposta.put("email", usuario.getEmail());
            resposta.put("tipoUsuario", usuario.getTipoUsuario());

            // Apenas profissionais possuem Pessoa vinculada
            if (usuario.getPessoa() != null) {
                resposta.put(
                        "idProfissional",
                        usuario.getPessoa().getId()
                );
            }

            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException erro) {

            Map<String, Object> resposta = new LinkedHashMap<>();
            resposta.put("erro", erro.getMessage());

            return ResponseEntity.badRequest().body(resposta);
        }
    }
}
