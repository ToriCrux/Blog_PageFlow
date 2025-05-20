package br.edu.infnet.pageflow.entities;

import br.edu.infnet.pageflow.entities.ids.PostLikeRelationId;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "posts_likes")
@IdClass(PostLikeRelationId.class)
public class PostLikeRelation {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BlogUser user;

    public PostLikeRelation() {}

    public PostLikeRelation(Post post, BlogUser user) {
        this.post = post;
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public BlogUser getUser() {
        return user;
    }


}
