package br.edu.infnet.pageflow.entities.ids;

import java.io.Serializable;
import java.util.Objects;

public class PostLikeRelationId implements Serializable {
    private Integer post;
    private Integer user;

    public PostLikeRelationId() {}

    public PostLikeRelationId(Integer post, Integer user) {
        this.post = post;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostLikeRelationId that)) return false;
        return Objects.equals(post, that.post) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, user);
    }

}
