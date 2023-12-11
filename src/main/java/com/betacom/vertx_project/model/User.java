package com.betacom.vertx_project.model;

import io.vertx.core.json.JsonObject;

import java.util.UUID;

public class User {

  private UUID id;
  private String login;
  private String hashedPassword;

  public User() {
    // Pusty konstruktor wymagany przez Vert.x JSON Mapper
  }

  public User(String login, String hashedPassword) {
    this.id = UUID.randomUUID();
    this.login = login;
    this.hashedPassword = hashedPassword;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json.put("id", id.toString());
    json.put("login", login);
    json.put("hashedPassword", hashedPassword);
    return json;
  }
}
