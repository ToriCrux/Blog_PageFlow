package br.edu.infnet.pageflow.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("AUTHOR")
public class Author extends User {

    @Column(length = 500)
    private String bio;

//    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Post> posts;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
