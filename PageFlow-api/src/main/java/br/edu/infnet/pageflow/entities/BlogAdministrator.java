package br.edu.infnet.pageflow.entities;

import br.edu.infnet.pageflow.utils.AdministratorRoles;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@NoArgsConstructor
@DiscriminatorValue("ADMINISTRATOR")
public class BlogAdministrator extends BlogUser {

    @Enumerated(EnumType.STRING)
    private AdministratorRoles adminRole;

}