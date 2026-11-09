
package com.audiovisualstudio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roteirista")
@Getter
@Setter
@NoArgsConstructor
public class Roteirista extends Pessoa {

    private float salario;
    private String especialidade;

    public Roteirista(String nome, String nacionalidade,
                      int idade, String cpf, float salario,
                      String especialidade) {

        super(nome, nacionalidade, idade, cpf);

        this.salario = salario;
        this.especialidade = especialidade;
    }
}
