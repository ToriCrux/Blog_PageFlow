package br.edu.infnet.pageflow.dto;

public class CommentRequest {

    private String content;
    private boolean approved = false;
    private Integer parentCommentId = null;
    private Integer authorId = null;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Integer getParentCommentId() {return parentCommentId;}

    public void setParentCommentId(Integer parentCommentId) {this.parentCommentId = parentCommentId;}

    public Integer getAuthorId() {return authorId;}

    public void setAuthorId(Integer authorId) {this.authorId = authorId;}
}
