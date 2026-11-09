
package com.audiovisualstudio.service;

import com.audiovisualstudio.model.Filme;
import com.audiovisualstudio.model.Ator;
import com.audiovisualstudio.model.Diretor;
import com.audiovisualstudio.model.Roteirista;

import com.audiovisualstudio.repository.FilmeRepository;
import com.audiovisualstudio.repository.AtorRepository;
import com.audiovisualstudio.repository.DiretorRepository;
import com.audiovisualstudio.repository.RoteiristaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
public class FilmeService {

    private final FilmeRepository repository;
    private final AtorRepository atorRepository;
    private final DiretorRepository diretorRepository;
    private final RoteiristaRepository roteiristaRepository;

    // Injeção de dependência
    public FilmeService(FilmeRepository repository,
                        AtorRepository atorRepository,
                        DiretorRepository diretorRepository,
                        RoteiristaRepository roteiristaRepository) {

        this.repository = repository;
        this.atorRepository = atorRepository;
        this.diretorRepository = diretorRepository;
        this.roteiristaRepository = roteiristaRepository;
    }

    // RF13 - Cadastrar filme
    public Filme cadastrar(Filme filme) {
        filme.setCodigo(null);
        return repository.save(filme);
    }

    // RF15 - Consultar todos
    public List<Filme> consultarTodos() {
        return repository.findAll();
    }

    // RF15 - Consultar por código
    public Optional<Filme> consultarPorId(Long codigo) {
        return repository.findById(codigo);
    }

    // RF14 - Editar filme
    public Filme editar(Long codigo, Filme dadosAtualizados) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        if (filmeEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();

            filme.setTitulo(dadosAtualizados.getTitulo());
            filme.setGenero(dadosAtualizados.getGenero());
            filme.setDuracao(dadosAtualizados.getDuracao());

            return repository.save(filme);

        } else {
            return null;
        }
    }

    // RF16 - Excluir filme
    public boolean excluir(Long codigo) {

        if (repository.existsById(codigo)) {
            repository.deleteById(codigo);
            return true;
        } else {
            return false;
        }
    }

    // =====================================
    // ATORES
    // =====================================

    // RF17 - Associar ator
    @Transactional
    public boolean associarAtor(Long codigo, Long idAtor) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        Optional<Ator> atorEncontrado =
                atorRepository.findById(idAtor);

        if (filmeEncontrado.isPresent()
                && atorEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();
            Ator ator = atorEncontrado.get();

            filme.getAtores().add(ator);
            repository.save(filme);

            return true;

        } else {
            return false;
        }
    }

    // RF20 - Remover ator
    @Transactional
    public boolean removerAtor(Long codigo, Long idAtor) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        Optional<Ator> atorEncontrado =
                atorRepository.findById(idAtor);

        if (filmeEncontrado.isPresent()
                && atorEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();
            Ator ator = atorEncontrado.get();

            boolean removido =
                    filme.getAtores().remove(ator);

            if (removido) {
                repository.save(filme);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    // RF23 - Consultar elenco
    @Transactional(readOnly = true)
    public List<Ator> consultarElenco(Long codigo) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        if (filmeEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();
            return new ArrayList<>(filme.getAtores());

        } else {
            return null;
        }
    }

    // =====================================
    // DIRETORES
    // =====================================

    // RF18 - Associar diretor
    @Transactional
    public boolean associarDiretor(Long codigo, Long idDiretor) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        Optional<Diretor> diretorEncontrado =
                diretorRepository.findById(idDiretor);

        if (filmeEncontrado.isPresent()
                && diretorEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();
            Diretor diretor = diretorEncontrado.get();

            filme.getDiretores().add(diretor);
            repository.save(filme);

            return true;

        } else {
            return false;
        }
    }

    // RF21 - Remover diretor
    @Transactional
    public boolean removerDiretor(Long codigo, Long idDiretor) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        Optional<Diretor> diretorEncontrado =
                diretorRepository.findById(idDiretor);

        if (filmeEncontrado.isPresent()
                && diretorEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();
            Diretor diretor = diretorEncontrado.get();

            boolean removido =
                    filme.getDiretores().remove(diretor);

            if (removido) {
                repository.save(filme);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    // RF24 - Consultar diretores
    @Transactional(readOnly = true)
    public List<Diretor> consultarDiretores(Long codigo) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        if (filmeEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();
            return new ArrayList<>(filme.getDiretores());

        } else {
            return null;
        }
    }

    // =====================================
    // ROTEIRISTAS
    // =====================================

    // RF19 - Associar roteirista
    @Transactional
    public boolean associarRoteirista(
            Long codigo, Long idRoteirista) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        Optional<Roteirista> roteiristaEncontrado =
                roteiristaRepository.findById(idRoteirista);

        if (filmeEncontrado.isPresent()
                && roteiristaEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();
            Roteirista roteirista = roteiristaEncontrado.get();

            filme.getRoteiristas().add(roteirista);
            repository.save(filme);

            return true;

        } else {
            return false;
        }
    }

    // RF22 - Remover roteirista
    @Transactional
    public boolean removerRoteirista(
            Long codigo, Long idRoteirista) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        Optional<Roteirista> roteiristaEncontrado =
                roteiristaRepository.findById(idRoteirista);

        if (filmeEncontrado.isPresent()
                && roteiristaEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();
            Roteirista roteirista = roteiristaEncontrado.get();

            boolean removido =
                    filme.getRoteiristas().remove(roteirista);

            if (removido) {
                repository.save(filme);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    // RF24 - Consultar roteiristas
    @Transactional(readOnly = true)
    public List<Roteirista> consultarRoteiristas(Long codigo) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        if (filmeEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();
            return new ArrayList<>(filme.getRoteiristas());

        } else {
            return null;
        }
    }

    // =====================================
    // CONSULTAS - RF26 A RF29
    // =====================================

    // RF26 - Consultar todos os profissionais de um filme
    @Transactional(readOnly = true)
    public Map<String, Object> consultarProfissionais(Long codigo) {

        Optional<Filme> filmeEncontrado =
                repository.findById(codigo);

        if (filmeEncontrado.isPresent()) {

            Filme filme = filmeEncontrado.get();

            Map<String, Object> resultado = new HashMap<>();

            resultado.put("codigo", filme.getCodigo());
            resultado.put("titulo", filme.getTitulo());

            List<Map<String, Object>> atores = new ArrayList<>();
            List<Map<String, Object>> diretores = new ArrayList<>();
            List<Map<String, Object>> roteiristas = new ArrayList<>();

            for (Ator ator : filme.getAtores()) {

                Map<String, Object> dados = new HashMap<>();
                dados.put("id", ator.getId());
                dados.put("nome", ator.getNome());

                atores.add(dados);
            }

            for (Diretor diretor : filme.getDiretores()) {

                Map<String, Object> dados = new HashMap<>();
                dados.put("id", diretor.getId());
                dados.put("nome", diretor.getNome());

                diretores.add(dados);
            }

            for (Roteirista roteirista : filme.getRoteiristas()) {

                Map<String, Object> dados = new HashMap<>();
                dados.put("id", roteirista.getId());
                dados.put("nome", roteirista.getNome());

                roteiristas.add(dados);
            }

            resultado.put("atores", atores);
            resultado.put("diretores", diretores);
            resultado.put("roteiristas", roteiristas);

            return resultado;

        } else {
            return null;
        }
    }

    // RF27 - Consultar filmes de um ator
    @Transactional(readOnly = true)
    public List<Map<String, Object>> consultarFilmesDoAtor(
            Long idAtor) {

        if (!atorRepository.existsById(idAtor)) {
            return null;
        }

        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Filme filme : repository.findAll()) {

            for (Ator ator : filme.getAtores()) {

                if (ator.getId().equals(idAtor)) {

                    Map<String, Object> dados = new HashMap<>();

                    dados.put("codigo", filme.getCodigo());
                    dados.put("titulo", filme.getTitulo());
                    dados.put("genero", filme.getGenero());
                    dados.put("duracao", filme.getDuracao());

                    resultado.add(dados);
                }
            }
        }

        return resultado;
    }

    // RF28 - Consultar filmes de um diretor
    @Transactional(readOnly = true)
    public List<Map<String, Object>> consultarFilmesDoDiretor(
            Long idDiretor) {

        if (!diretorRepository.existsById(idDiretor)) {
            return null;
        }

        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Filme filme : repository.findAll()) {

            for (Diretor diretor : filme.getDiretores()) {

                if (diretor.getId().equals(idDiretor)) {

                    Map<String, Object> dados = new HashMap<>();

                    dados.put("codigo", filme.getCodigo());
                    dados.put("titulo", filme.getTitulo());
                    dados.put("genero", filme.getGenero());
                    dados.put("duracao", filme.getDuracao());

                    resultado.add(dados);
                }
            }
        }

        return resultado;
    }

    // RF29 - Consultar filmes de um roteirista
    @Transactional(readOnly = true)
    public List<Map<String, Object>> consultarFilmesDoRoteirista(
            Long idRoteirista) {

        if (!roteiristaRepository.existsById(idRoteirista)) {
            return null;
        }

        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Filme filme : repository.findAll()) {

            for (Roteirista roteirista : filme.getRoteiristas()) {

                if (roteirista.getId().equals(idRoteirista)) {

                    Map<String, Object> dados = new HashMap<>();

                    dados.put("codigo", filme.getCodigo());
                    dados.put("titulo", filme.getTitulo());
                    dados.put("genero", filme.getGenero());
                    dados.put("duracao", filme.getDuracao());

                    resultado.add(dados);
                }
            }
        }

        return resultado;
    }
}
