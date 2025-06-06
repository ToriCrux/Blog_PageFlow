package br.edu.infnet.pageflow.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("AUTHOR")
public class Author extends BlogUser {

    @Column(length = 500)
    private String bio;

    public Author(String firstName, String username, String email, String password) {
        super(firstName, username, email, password);
    }

    public Author(String email, String password) {
        super(email, password);
    }

}
