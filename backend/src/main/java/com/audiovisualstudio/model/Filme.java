
package com.audiovisualstudio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "filme")
@Getter
@Setter
@NoArgsConstructor
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private String titulo;
    private String genero;
    private int duracao;

    // RF17 - Atores associados ao filme
    @ManyToMany
    @JoinTable(
            name = "filme_ator",
            joinColumns = @JoinColumn(name = "filme_codigo"),
            inverseJoinColumns = @JoinColumn(name = "ator_id")
    )
    private Set<Ator> atores = new HashSet<>();

    // RF18 - Diretores associados ao filme
    @ManyToMany
    @JoinTable(
            name = "filme_diretor",
            joinColumns = @JoinColumn(name = "filme_codigo"),
            inverseJoinColumns = @JoinColumn(name = "diretor_id")
    )
    private Set<Diretor> diretores = new HashSet<>();

    // RF19 - Roteiristas associados ao filme
    @ManyToMany
    @JoinTable(
            name = "filme_roteirista",
            joinColumns = @JoinColumn(name = "filme_codigo"),
            inverseJoinColumns = @JoinColumn(name = "roteirista_id")
    )
    private Set<Roteirista> roteiristas = new HashSet<>();

    // Construtor
    public Filme(String titulo, String genero, int duracao) {
        this.titulo = titulo;
        this.genero = genero;
        this.duracao = duracao;
    }
}
