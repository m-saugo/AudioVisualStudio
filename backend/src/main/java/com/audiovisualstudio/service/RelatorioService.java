
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
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class RelatorioService {

    private final FilmeRepository filmeRepository;
    private final AtorRepository atorRepository;
    private final DiretorRepository diretorRepository;
    private final RoteiristaRepository roteiristaRepository;

    // Injeção de dependência
    public RelatorioService(
            FilmeRepository filmeRepository,
            AtorRepository atorRepository,
            DiretorRepository diretorRepository,
            RoteiristaRepository roteiristaRepository) {

        this.filmeRepository = filmeRepository;
        this.atorRepository = atorRepository;
        this.diretorRepository = diretorRepository;
        this.roteiristaRepository = roteiristaRepository;
    }

    // ========================================
    // RF25 e RF30 - RELATÓRIO DE FILMES
    // ========================================

    @Transactional(readOnly = true)
    public Map<String, Object> relatorioFilmes() {

        List<Filme> filmes = filmeRepository.findAll();

        List<Map<String, Object>> lista = new ArrayList<>();

        for (Filme filme : filmes) {

            Map<String, Object> dados = new LinkedHashMap<>();

            dados.put("codigo", filme.getCodigo());
            dados.put("titulo", filme.getTitulo());
            dados.put("genero", filme.getGenero());
            dados.put("duracao", filme.getDuracao());

            lista.add(dados);
        }

        Map<String, Object> relatorio = new LinkedHashMap<>();

        relatorio.put("totalFilmes", filmes.size());
        relatorio.put("filmes", lista);

        return relatorio;
    }

    // ========================================
    // RF31 - RELATÓRIO DE PROFISSIONAIS
    // ========================================

    @Transactional(readOnly = true)
    public Map<String, Object> relatorioProfissionais() {

        List<Ator> atores = atorRepository.findAll();

        List<Diretor> diretores = diretorRepository.findAll();

        List<Roteirista> roteiristas =
                roteiristaRepository.findAll();

        List<Map<String, Object>> listaAtores = new ArrayList<>();
        List<Map<String, Object>> listaDiretores = new ArrayList<>();
        List<Map<String, Object>> listaRoteiristas = new ArrayList<>();

        // Atores
        for (Ator ator : atores) {

            Map<String, Object> dados = new LinkedHashMap<>();

            dados.put("id", ator.getId());
            dados.put("nome", ator.getNome());
            dados.put("profissao", "Ator");

            listaAtores.add(dados);
        }

        // Diretores
        for (Diretor diretor : diretores) {

            Map<String, Object> dados = new LinkedHashMap<>();

            dados.put("id", diretor.getId());
            dados.put("nome", diretor.getNome());
            dados.put("profissao", "Diretor");

            listaDiretores.add(dados);
        }

        // Roteiristas
        for (Roteirista roteirista : roteiristas) {

            Map<String, Object> dados = new LinkedHashMap<>();

            dados.put("id", roteirista.getId());
            dados.put("nome", roteirista.getNome());
            dados.put("profissao", "Roteirista");

            listaRoteiristas.add(dados);
        }

        Map<String, Object> relatorio = new LinkedHashMap<>();

        relatorio.put("totalAtores", atores.size());
        relatorio.put("totalDiretores", diretores.size());
        relatorio.put("totalRoteiristas", roteiristas.size());

        relatorio.put(
                "totalProfissionais",
                atores.size() + diretores.size() + roteiristas.size()
        );

        relatorio.put("atores", listaAtores);
        relatorio.put("diretores", listaDiretores);
        relatorio.put("roteiristas", listaRoteiristas);

        return relatorio;
    }

    // ========================================
    // RF32 - RELATÓRIO DE ELENCOS E EQUIPES
    // ========================================

    @Transactional(readOnly = true)
    public Map<String, Object> relatorioElencosEquipes() {

        List<Filme> filmes = filmeRepository.findAll();

        List<Map<String, Object>> producoes = new ArrayList<>();

        for (Filme filme : filmes) {

            Map<String, Object> producao = new LinkedHashMap<>();

            producao.put("codigo", filme.getCodigo());
            producao.put("titulo", filme.getTitulo());

            List<Map<String, Object>> atores = new ArrayList<>();
            List<Map<String, Object>> diretores = new ArrayList<>();
            List<Map<String, Object>> roteiristas = new ArrayList<>();

            // Elenco
            for (Ator ator : filme.getAtores()) {

                Map<String, Object> dados = new LinkedHashMap<>();

                dados.put("id", ator.getId());
                dados.put("nome", ator.getNome());

                atores.add(dados);
            }

            // Direção
            for (Diretor diretor : filme.getDiretores()) {

                Map<String, Object> dados = new LinkedHashMap<>();

                dados.put("id", diretor.getId());
                dados.put("nome", diretor.getNome());

                diretores.add(dados);
            }

            // Roteiro
            for (Roteirista roteirista : filme.getRoteiristas()) {

                Map<String, Object> dados = new LinkedHashMap<>();

                dados.put("id", roteirista.getId());
                dados.put("nome", roteirista.getNome());

                roteiristas.add(dados);
            }

            producao.put("elenco", atores);
            producao.put("diretores", diretores);
            producao.put("roteiristas", roteiristas);

            producao.put("totalAtores", atores.size());
            producao.put("totalDiretores", diretores.size());
            producao.put("totalRoteiristas", roteiristas.size());

            producoes.add(producao);
        }

        Map<String, Object> relatorio = new LinkedHashMap<>();

        relatorio.put("totalFilmes", filmes.size());
        relatorio.put("producoes", producoes);

        return relatorio;
    }
}
