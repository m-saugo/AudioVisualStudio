
package com.audiovisualstudio.controller;

import com.audiovisualstudio.service.RelatorioService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService service;

    // Injeção de dependência
    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    // RF25 e RF30 - Relatório de filmes
    @GetMapping("/filmes")
    public Map<String, Object> relatorioFilmes() {
        return service.relatorioFilmes();
    }

    // RF31 - Relatório de profissionais
    @GetMapping("/profissionais")
    public Map<String, Object> relatorioProfissionais() {
        return service.relatorioProfissionais();
    }

    // RF32 - Relatório de elencos e equipes
    @GetMapping("/elencos-equipes")
    public Map<String, Object> relatorioElencosEquipes() {
        return service.relatorioElencosEquipes();
    }
}
