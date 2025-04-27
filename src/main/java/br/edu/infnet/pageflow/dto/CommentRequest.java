package br.edu.infnet.pageflow.dto;

public class CommentRequest {

    private String content;
    private boolean approved = false;

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
}
