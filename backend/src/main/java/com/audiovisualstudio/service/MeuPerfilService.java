
package com.audiovisualstudio.service;

import com.audiovisualstudio.dto.MeuPerfilRequest;

import com.audiovisualstudio.model.Usuario;
import com.audiovisualstudio.model.Pessoa;
import com.audiovisualstudio.model.Ator;
import com.audiovisualstudio.model.Diretor;
import com.audiovisualstudio.model.Roteirista;

import com.audiovisualstudio.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class MeuPerfilService {

    private final UsuarioRepository usuarioRepository;

    public MeuPerfilService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // RF35 - Consultar o próprio perfil
    @Transactional(readOnly = true)
    public Map<String, Object> consultarMeuPerfil(String email) {

        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findByEmail(email);

        if (usuarioEncontrado.isPresent()) {

            Usuario usuario = usuarioEncontrado.get();

            Map<String, Object> resposta = new LinkedHashMap<>();

            resposta.put("idUsuario", usuario.getId());
            resposta.put("email", usuario.getEmail());
            resposta.put("tipoUsuario", usuario.getTipoUsuario());

            if (usuario.getPessoa() != null) {

                Pessoa pessoa = usuario.getPessoa();

                resposta.put("idProfissional", pessoa.getId());
                resposta.put("nome", pessoa.getNome());
                resposta.put("nacionalidade", pessoa.getNacionalidade());
                resposta.put("idade", pessoa.getIdade());
                resposta.put("cpf", pessoa.getCpf());

                // Dados específicos de cada profissão
                if (pessoa instanceof Ator) {

                    Ator ator = (Ator) pessoa;

                    resposta.put("cache", ator.getCache());
                    resposta.put("papel", ator.getPapel());
                    resposta.put("tipoPersonagem", ator.getTipoPersonagem());

                } else if (pessoa instanceof Diretor) {

                    Diretor diretor = (Diretor) pessoa;

                    resposta.put("salario", diretor.getSalario());
                    resposta.put("estiloDirecao", diretor.getEstiloDirecao());

                } else if (pessoa instanceof Roteirista) {

                    Roteirista roteirista = (Roteirista) pessoa;

                    resposta.put("salario", roteirista.getSalario());
                    resposta.put("especialidade", roteirista.getEspecialidade());
                }
            }

            return resposta;

        } else {
            return null;
        }
    }

    // RF35 - Editar os próprios dados
    @Transactional
    public Map<String, Object> editarMeuPerfil(
            String email,
            MeuPerfilRequest dados) {

        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findByEmail(email);

        if (usuarioEncontrado.isPresent()) {

            Usuario usuario = usuarioEncontrado.get();

            Pessoa pessoa = usuario.getPessoa();

            if (pessoa == null) {
                return null;
            }

            // Atualizar dados comuns
            if (dados.getNome() != null) {
                if (dados.getNome().isBlank()) {
                    throw new IllegalArgumentException(
                            "O nome não pode ficar vazio."
                    );
                }
                pessoa.setNome(dados.getNome());
            }

            if (dados.getNacionalidade() != null) {
                pessoa.setNacionalidade(
                        dados.getNacionalidade()
                );
            }

            if (dados.getIdade() != null) {
                if (dados.getIdade() <= 0) {
                    throw new IllegalArgumentException(
                            "A idade deve ser maior que zero."
                    );
                }
                pessoa.setIdade(dados.getIdade());
            }

            if (dados.getCpf() != null) {
                if (dados.getCpf().isBlank()) {
                    throw new IllegalArgumentException(
                            "O CPF não pode ficar vazio."
                    );
                }
                pessoa.setCpf(dados.getCpf());
            }

            // Dados específicos de cada profissão
            if (pessoa instanceof Ator) {

                Ator ator = (Ator) pessoa;

                if (dados.getCache() != null) {
                    ator.setCache(dados.getCache());
                }

                if (dados.getPapel() != null) {
                    ator.setPapel(dados.getPapel());
                }

                if (dados.getTipoPersonagem() != null) {
                    ator.setTipoPersonagem(
                            dados.getTipoPersonagem()
                    );
                }

            } else if (pessoa instanceof Diretor) {

                Diretor diretor = (Diretor) pessoa;

                if (dados.getSalario() != null) {
                    diretor.setSalario(dados.getSalario());
                }

                if (dados.getEstiloDirecao() != null) {
                    diretor.setEstiloDirecao(
                            dados.getEstiloDirecao()
                    );
                }

            } else if (pessoa instanceof Roteirista) {

                Roteirista roteirista = (Roteirista) pessoa;

                if (dados.getSalario() != null) {
                    roteirista.setSalario(dados.getSalario());
                }

                if (dados.getEspecialidade() != null) {
                    roteirista.setEspecialidade(
                            dados.getEspecialidade()
                    );
                }
            }

            // O JPA salva as alterações ao finalizar
            // a transação, pois a entidade está gerenciada.

            Map<String, Object> resposta = new LinkedHashMap<>();

            resposta.put(
                    "mensagem",
                    "Dados atualizados com sucesso!"
            );
            resposta.put("idProfissional", pessoa.getId());
            resposta.put("nome", pessoa.getNome());
            resposta.put(
                    "tipoUsuario",
                    usuario.getTipoUsuario()
            );

            return resposta;

        } else {
            return null;
        }
    }
}
