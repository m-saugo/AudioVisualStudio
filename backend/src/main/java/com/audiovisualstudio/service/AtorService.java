
package com.audiovisualstudio.service;

import com.audiovisualstudio.model.Ator;
import com.audiovisualstudio.repository.AtorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

@Service
public class AtorService {

    private final AtorRepository repository;

    public AtorService(AtorRepository repository) {
        this.repository = repository;
    }

    // RF01 - Cadastrar ator
    public Ator cadastrar(Ator ator) {
        ator.setId(null);
        return repository.save(ator);
    }

    // RF03 - Consultar todos os atores
    public List<Ator> consultarTodos() {
        return repository.findAll();
    }

    // RF03 - Consultar um ator pelo ID
    public Optional<Ator> consultarPorId(Long id) {
        return repository.findById(id);
    }

    // RF02 - Editar ator
    public Ator editar(Long id, Ator dadosAtualizados) {

        Optional<Ator> atorEncontrado = repository.findById(id);

        if (atorEncontrado.isPresent()) {

            Ator ator = atorEncontrado.get();

            ator.setNome(dadosAtualizados.getNome());
            ator.setNacionalidade(dadosAtualizados.getNacionalidade());
            ator.setIdade(dadosAtualizados.getIdade());
            ator.setCpf(dadosAtualizados.getCpf());

            ator.setCache(dadosAtualizados.getCache());
            ator.setPapel(dadosAtualizados.getPapel());
            ator.setTipoPersonagem(
                    dadosAtualizados.getTipoPersonagem()
            );

            return repository.save(ator);

        } else {
            return null;
        }
    }

    // RF04 - Excluir ator
    public boolean excluir(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
