
package com.audiovisualstudio.service;

import com.audiovisualstudio.model.Diretor;
import com.audiovisualstudio.repository.DiretorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiretorService {

    private final DiretorRepository repository;

    // Construtor
    public DiretorService(DiretorRepository repository) {
        this.repository = repository;
    }

    // RF05 - Cadastrar diretor
    public Diretor cadastrar(Diretor diretor) {
        diretor.setId(null);
        return repository.save(diretor);
    }

    // RF07 - Consultar todos os diretores
    public List<Diretor> consultarTodos() {
        return repository.findAll();
    }

    // RF07 - Consultar diretor pelo ID
    public Optional<Diretor> consultarPorId(Long id) {
        return repository.findById(id);
    }

    // RF06 - Editar diretor
    public Diretor editar(Long id, Diretor dadosAtualizados) {

        Optional<Diretor> diretorEncontrado =
                repository.findById(id);

        if (diretorEncontrado.isPresent()) {

            Diretor diretor = diretorEncontrado.get();

            // Dados herdados de Pessoa
            diretor.setNome(dadosAtualizados.getNome());
            diretor.setNacionalidade(
                    dadosAtualizados.getNacionalidade()
            );
            diretor.setIdade(dadosAtualizados.getIdade());
            diretor.setCpf(dadosAtualizados.getCpf());

            // Dados específicos de Diretor
            diretor.setSalario(dadosAtualizados.getSalario());
            diretor.setEstiloDirecao(
                    dadosAtualizados.getEstiloDirecao()
            );

            return repository.save(diretor);

        } else {
            return null;
        }
    }

    // RF08 - Excluir diretor
    public boolean excluir(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
