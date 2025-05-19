package br.edu.infnet.pageflow.entities.ids;

import java.io.Serializable;
import java.util.Objects;

public class CommentLikeRelationId implements Serializable {
    private Integer comment;
    private Integer user;

    public CommentLikeRelationId() {}

    public CommentLikeRelationId(Integer comment, Integer user) {
        this.comment = comment;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentLikeRelationId that)) return false;
        return Objects.equals(comment, that.comment) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, user);
    }
}
