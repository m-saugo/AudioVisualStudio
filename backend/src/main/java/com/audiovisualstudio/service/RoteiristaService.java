
package com.audiovisualstudio.service;

import com.audiovisualstudio.model.Roteirista;
import com.audiovisualstudio.repository.RoteiristaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoteiristaService {

    private final RoteiristaRepository repository;

    public RoteiristaService(RoteiristaRepository repository) {
        this.repository = repository;
    }

    // RF09 - Cadastrar roteirista
    public Roteirista cadastrar(Roteirista roteirista) {
        roteirista.setId(null);
        return repository.save(roteirista);
    }

    // RF11 - Consultar todos os roteiristas
    public List<Roteirista> consultarTodos() {
        return repository.findAll();
    }

    // RF11 - Consultar roteirista pelo ID
    public Optional<Roteirista> consultarPorId(Long id) {
        return repository.findById(id);
    }

    // RF10 - Editar roteirista
    public Roteirista editar(Long id,
                             Roteirista dadosAtualizados) {

        Optional<Roteirista> roteiristaEncontrado =
                repository.findById(id);

        if (roteiristaEncontrado.isPresent()) {

            Roteirista roteirista = roteiristaEncontrado.get();

            // Dados herdados de Pessoa
            roteirista.setNome(dadosAtualizados.getNome());
            roteirista.setNacionalidade(
                    dadosAtualizados.getNacionalidade()
            );
            roteirista.setIdade(dadosAtualizados.getIdade());
            roteirista.setCpf(dadosAtualizados.getCpf());

            // Dados específicos de Roteirista
            roteirista.setSalario(dadosAtualizados.getSalario());
            roteirista.setEspecialidade(
                    dadosAtualizados.getEspecialidade()
            );

            return repository.save(roteirista);

        } else {
            return null;
        }
    }

    // RF12 - Excluir roteirista
    public boolean excluir(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
