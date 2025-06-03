package br.edu.infnet.pageflow.dto;

import br.edu.infnet.pageflow.entities.Author;
import br.edu.infnet.pageflow.entities.Category;

import java.util.List;

public class PostResponse {

    private Integer id;
    private String title;
    private String content;
    private Author author;
    private Category category;
    private List<CommentResponse> comments;

    public PostResponse() {}

    public PostResponse(Integer id, String title, String content, Author author, Category category, List<CommentResponse> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
    }
}
