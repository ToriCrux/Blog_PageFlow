package br.edu.infnet.pageflow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    private int id;
    private String content;
    private boolean approved = false;

//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public boolean isApproved() {
//        return approved;
//    }
//
//    public void setApproved(boolean approved) {
//        this.approved = approved;
//    }
}
