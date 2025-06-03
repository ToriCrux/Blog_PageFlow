package br.edu.infnet.pageflow.entities;

import jakarta.persistence.*;

@Entity
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

    public Author() {
        super();
    }

//    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Post> posts;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
