
package com.audiovisualstudio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String nacionalidade;
    private int idade;
    private String cpf;

    // Construtor com parâmetros
    public Pessoa(String nome, String nacionalidade,
                  int idade, String cpf) {

        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.idade = idade;
        this.cpf = cpf;
    }
}
