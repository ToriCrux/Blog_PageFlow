package br.edu.infnet.pageflow.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("VISITOR")
public class Visitor extends BlogUser {

    @Column
    private Boolean canComment;

}
