package br.edu.infnet.pageflow.dto;

public class NewPasswordRequest {

    private String oldPassword;

    private String token;

    private String newPassword;

    public NewPasswordRequest(String oldPassword, String token, String newPassword) {
        this.oldPassword = oldPassword;
        this.token = token;
        this.newPassword = newPassword;
    }

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
