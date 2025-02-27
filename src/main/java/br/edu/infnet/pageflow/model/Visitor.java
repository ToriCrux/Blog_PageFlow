package br.edu.infnet.pageflow.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("VISITOR")
public class Visitor extends User {

    @Column
    private Boolean canComment;

//    @Column
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments;

    // Getters e Setters
    public Boolean getCanComment() {
        return canComment;
    }

    public void setCanComment(Boolean canComment) {
        this.canComment = canComment;
    }
}
