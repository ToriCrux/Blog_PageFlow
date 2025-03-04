package br.edu.infnet.pageflow.dto;

public class NewPasswordRequest {

    private String oldPassword;

    private String token;

    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getToken() {
        return token;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
