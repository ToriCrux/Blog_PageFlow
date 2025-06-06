package br.edu.infnet.pageflow.dto;

public class CommentResponse {

    private Integer id;
    private String content;
    private boolean approved;

    public CommentResponse(Integer id, String content, boolean approved) {
        this.id = id;
        this.content = content;
        this.approved = approved;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
