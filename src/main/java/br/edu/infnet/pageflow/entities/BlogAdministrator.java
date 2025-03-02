package br.edu.infnet.pageflow.entities;

import br.edu.infnet.pageflow.utils.AdministratorRoles;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("ADMINISTRATOR")
public class BlogAdministrator extends BlogUser {

    @Enumerated(EnumType.STRING)
    private AdministratorRoles adminRole;

    public AdministratorRoles getAdminRole() {
        return adminRole;
    }

    public void setRole(AdministratorRoles adminRole) {
        this.adminRole = adminRole;
    }
}