
package com.audiovisualstudio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ator")
@Getter
@Setter
@NoArgsConstructor
public class Ator extends Pessoa {

    private float cache;
    private String papel;
    private String tipoPersonagem;

    // Construtor com todos os dados
    public Ator(String nome, String nacionalidade,
                int idade, String cpf, float cache,
                String papel, String tipoPersonagem) {

        super(nome, nacionalidade, idade, cpf);

        this.cache = cache;
        this.papel = papel;
        this.tipoPersonagem = tipoPersonagem;
    }
}
