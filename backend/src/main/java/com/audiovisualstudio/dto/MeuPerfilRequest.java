
package com.audiovisualstudio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class MeuPerfilRequest {

    private String nome;
    private String nacionalidade;
    private Integer idade;
    private String cpf;

    // Ator
    private Float cache;
    private String papel;
    private String tipoPersonagem;

    // Diretor e roteirista
    private Float salario;

    // Diretor
    private String estiloDirecao;

    // Roteirista
    private String especialidade;
}
