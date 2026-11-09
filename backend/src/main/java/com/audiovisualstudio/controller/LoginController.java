
package com.audiovisualstudio.controller;

import com.audiovisualstudio.dto.LoginRequest;
import com.audiovisualstudio.model.TipoUsuario;
import com.audiovisualstudio.model.Usuario;
import com.audiovisualstudio.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService service;

    private final HttpSessionSecurityContextRepository
            contextRepository =
            new HttpSessionSecurityContextRepository();

    public LoginController(LoginService service) {
        this.service = service;
    }

    // RF34 - Login com escolha de perfil
    @PostMapping
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest dados,
            HttpServletRequest request,
            HttpServletResponse response) {

        Map<String, Object> resposta = new LinkedHashMap<>();

        if (dados == null
                || dados.getEntrarComo() == null) {

            resposta.put("erro", "Informe os dados e o tipo de acesso.");
            return ResponseEntity.badRequest().body(resposta);
        }

        String entrarComo =
                dados.getEntrarComo().trim().toUpperCase();

        if (!entrarComo.equals("ADMINISTRADOR")
                && !entrarComo.equals("PROFISSIONAL")) {

            resposta.put("erro", "Tipo de acesso invalido.");
            return ResponseEntity.badRequest().body(resposta);
        }

        // Verificar email e senha
        Usuario usuario = service.verificarCredenciais(
                dados.getEmail(),
                dados.getSenha()
        );

        if (usuario == null) {

            resposta.put("erro", "Email ou senha incorretos.");
            return ResponseEntity.status(401).body(resposta);
        }

        // Conferir o perfil verdadeiro da conta
        if (entrarComo.equals("ADMINISTRADOR")) {

            if (usuario.getTipoUsuario()
                    != TipoUsuario.ADMINISTRADOR) {

                resposta.put("erro", "Acesso negado.");
                return ResponseEntity.status(403).body(resposta);
            }

        } else {

            if (usuario.getTipoUsuario()
                    == TipoUsuario.ADMINISTRADOR) {

                resposta.put("erro", "Acesso negado.");
                return ResponseEntity.status(403).body(resposta);
            }
        }

        // Criar sessao autenticada
        request.getSession(true);
        request.changeSessionId();

        String perfil = "ROLE_"
                + usuario.getTipoUsuario().name();

        UsernamePasswordAuthenticationToken autenticacao =
                new UsernamePasswordAuthenticationToken(
                        usuario.getEmail(),
                        null,
                        List.of(
                                new SimpleGrantedAuthority(perfil)
                        )
                );

        SecurityContext contexto =
                SecurityContextHolder.createEmptyContext();

        contexto.setAuthentication(autenticacao);
        SecurityContextHolder.setContext(contexto);

        contextRepository.saveContext(
                contexto,
                request,
                response
        );

        resposta.put("mensagem", "Login realizado com sucesso!");
        resposta.put("idUsuario", usuario.getId());
        resposta.put("email", usuario.getEmail());
        resposta.put("tipoUsuario", usuario.getTipoUsuario());

        return ResponseEntity.ok(resposta);
    }

    // Encerrar sessao
    @PostMapping("/sair")
    public ResponseEntity<Map<String, String>> sair(
            HttpServletRequest request) {

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(
                Map.of("mensagem", "Sessao encerrada.")
        );
    }
}
