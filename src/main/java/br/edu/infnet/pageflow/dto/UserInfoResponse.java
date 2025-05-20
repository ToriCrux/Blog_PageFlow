package br.edu.infnet.pageflow.dto;

import jakarta.persistence.Column;

public class UserInfoResponse {
    private Integer id;
    private String name;
    private String username;
    private String email;

    public UserInfoResponse() {}

    public UserInfoResponse(Integer id, String name, String username, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
}
