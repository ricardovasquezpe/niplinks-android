package com.nippylinks.android.classes;

/**
 * Created by kevincampo on 20/03/17.
 */

public class CustomLink {
    private String name;
    private int id;
    private String color;
    private String login_url;
    private String username;
    private String password;
    private String usernameField;
    private String passwordField;
    private String buttonField;
    private String formField;

    public CustomLink(int id, String name,  String color, String login_url, String username, String password, String username_field, String password_field, String button_field, String form_field) {
        this.setId(id);
        this.setName(name);
        this.setColor(color);
        this.setLogin_url(login_url);
        this.setUsername(username);
        this.setPassword(password);
        this.setUsernameField(username_field);
        this.setPasswordField(password_field);
        this.setButtonField(button_field);
        this.setFormField(form_field);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLogin_url() {
        return login_url;
    }

    public void setLogin_url(String login_url) {
        this.login_url = login_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsernameField() {
        return usernameField;
    }

    public void setUsernameField(String usernameField) {
        this.usernameField = usernameField;
    }

    public String getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(String passwordField) {
        this.passwordField = passwordField;
    }

    public String getButtonField() {
        return buttonField;
    }

    public void setButtonField(String buttonField) {
        this.buttonField = buttonField;
    }

    public String getFormField() {
        return formField;
    }

    public void setFormField(String formField) {
        this.formField = formField;
    }
}
