
package com.audiovisualstudio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    private String email;
    private String senha;

    // ADMINISTRADOR ou PROFISSIONAL
    private String entrarComo;
}
