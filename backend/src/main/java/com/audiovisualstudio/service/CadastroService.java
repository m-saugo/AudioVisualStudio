
package com.audiovisualstudio.service;

import com.audiovisualstudio.dto.CadastroRequest;

import com.audiovisualstudio.model.Pessoa;
import com.audiovisualstudio.model.Ator;
import com.audiovisualstudio.model.Diretor;
import com.audiovisualstudio.model.Roteirista;
import com.audiovisualstudio.model.Usuario;
import com.audiovisualstudio.model.TipoUsuario;

import com.audiovisualstudio.repository.UsuarioRepository;
import com.audiovisualstudio.repository.AtorRepository;
import com.audiovisualstudio.repository.DiretorRepository;
import com.audiovisualstudio.repository.RoteiristaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Locale;

@Service
public class CadastroService {

    private final UsuarioRepository usuarioRepository;
    private final AtorRepository atorRepository;
    private final DiretorRepository diretorRepository;
    private final RoteiristaRepository roteiristaRepository;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public CadastroService(
            UsuarioRepository usuarioRepository,
            AtorRepository atorRepository,
            DiretorRepository diretorRepository,
            RoteiristaRepository roteiristaRepository) {

        this.usuarioRepository = usuarioRepository;
        this.atorRepository = atorRepository;
        this.diretorRepository = diretorRepository;
        this.roteiristaRepository = roteiristaRepository;
    }

    // RF33 - Cadastro de usuarios
    @Transactional
    public Usuario cadastrar(CadastroRequest dados) {

        // Verificar dados da conta
        if (dados == null
                || dados.getEmail() == null
                || dados.getEmail().isBlank()
                || dados.getSenha() == null
                || dados.getSenha().length() < 6
                || dados.getTipoUsuario() == null) {

            throw new IllegalArgumentException(
                    "Informe email, senha e tipo de usuario. " +
                            "A senha deve ter pelo menos 6 caracteres."
            );
        }

        String email = dados.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Este email ja esta cadastrado."
            );
        }

        Pessoa pessoa = null;

        // Administrador nao precisa de cadastro profissional
        if (dados.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {

            pessoa = null;

        } else {

            // Profissionais precisam preencher dados pessoais
            if (dados.getNome() == null
                    || dados.getNome().isBlank()
                    || dados.getCpf() == null
                    || dados.getCpf().isBlank()) {

                throw new IllegalArgumentException(
                        "Informe nome e CPF do profissional."
                );
            }

            if (dados.getTipoUsuario() == TipoUsuario.ATOR) {

                Ator ator = new Ator(
                        dados.getNome(),
                        dados.getNacionalidade(),
                        dados.getIdade(),
                        dados.getCpf(),
                        dados.getCache(),
                        dados.getPapel(),
                        dados.getTipoPersonagem()
                );

                pessoa = atorRepository.save(ator);

            } else if (dados.getTipoUsuario()
                    == TipoUsuario.DIRETOR) {

                Diretor diretor = new Diretor(
                        dados.getNome(),
                        dados.getNacionalidade(),
                        dados.getIdade(),
                        dados.getCpf(),
                        dados.getSalario(),
                        dados.getEstiloDirecao()
                );

                pessoa = diretorRepository.save(diretor);

            } else if (dados.getTipoUsuario()
                    == TipoUsuario.ROTEIRISTA) {

                Roteirista roteirista = new Roteirista(
                        dados.getNome(),
                        dados.getNacionalidade(),
                        dados.getIdade(),
                        dados.getCpf(),
                        dados.getSalario(),
                        dados.getEspecialidade()
                );

                pessoa = roteiristaRepository.save(roteirista);

            } else {
                throw new IllegalArgumentException(
                        "Tipo de usuario invalido."
                );
            }
        }

        String senhaHash = encoder.encode(dados.getSenha());

        Usuario usuario = new Usuario(
                email,
                senhaHash,
                dados.getTipoUsuario(),
                pessoa
        );

        return usuarioRepository.save(usuario);
    }
}
