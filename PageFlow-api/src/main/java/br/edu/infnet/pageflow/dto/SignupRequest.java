package br.edu.infnet.pageflow.dto;

import br.edu.infnet.pageflow.utils.BlogUserRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    private String name;
    private String username;
    private String email;
    private String password;
    private BlogUserRoles role;

}
