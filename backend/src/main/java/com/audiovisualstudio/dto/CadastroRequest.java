
package com.audiovisualstudio.dto;

import com.audiovisualstudio.model.TipoUsuario;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CadastroRequest {

    private String email;
    private String senha;
    private TipoUsuario tipoUsuario;

    private String nome;
    private String nacionalidade;
    private int idade;
    private String cpf;

    private float cache;
    private String papel;
    private String tipoPersonagem;

    private float salario;
    private String estiloDirecao;
    private String especialidade;
}
