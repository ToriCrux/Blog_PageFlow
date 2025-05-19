package br.edu.infnet.pageflow.entities;

import br.edu.infnet.pageflow.entities.ids.CommentLikeRelationId;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "comments_likes")
@IdClass(CommentLikeRelationId.class)
public class CommentLikeRelation {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment comment;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BlogUser user;

    public CommentLikeRelation() {}

    public CommentLikeRelation(Comment comment, BlogUser user) {
        this.comment = comment;
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public BlogUser getUser() {
        return user;
    }



}
