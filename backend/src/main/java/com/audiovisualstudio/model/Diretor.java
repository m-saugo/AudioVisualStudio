
package com.audiovisualstudio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diretor")
@Getter
@Setter
@NoArgsConstructor
public class Diretor extends Pessoa {

    private float salario;
    private String estiloDirecao;

    public Diretor(String nome, String nacionalidade,
                   int idade, String cpf, float salario,
                   String estiloDirecao) {

        super(nome, nacionalidade, idade, cpf);

        this.salario = salario;
        this.estiloDirecao = estiloDirecao;
    }
}
