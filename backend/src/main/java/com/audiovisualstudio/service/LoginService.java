
package com.audiovisualstudio.service;

import com.audiovisualstudio.model.Usuario;
import com.audiovisualstudio.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Locale;

@Service
public class LoginService {

    private final UsuarioRepository repository;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public LoginService(UsuarioRepository repository) {
        this.repository = repository;
    }

    // RF34 - Verificar e-mail e senha
    public Usuario verificarCredenciais(
            String email, String senha) {

        if (email == null || senha == null) {
            return null;
        }

        String emailFormatado =
                email.trim().toLowerCase(Locale.ROOT);

        Optional<Usuario> usuarioEncontrado =
                repository.findByEmail(emailFormatado);

        if (usuarioEncontrado.isPresent()) {

            Usuario usuario = usuarioEncontrado.get();

            if (encoder.matches(senha, usuario.getSenhaHash())) {
                return usuario;
            } else {
                return null;
            }

        } else {
            return null;
        }
    }
}
