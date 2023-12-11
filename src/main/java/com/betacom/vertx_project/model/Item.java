package com.betacom.vertx_project.model;

import io.vertx.core.json.JsonObject;

import java.util.UUID;

public class Item {

  private UUID id;
  private UUID owner;
  private String name;

  public Item() {
    // Pusty konstruktor wymagany przez Vert.x JSON Mapper
  }

  public Item(UUID owner, String name) {
    this.id = UUID.randomUUID();
    this.owner = owner;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getOwner() {
    return owner;
  }

  public void setOwner(UUID owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json.put("id", id.toString());
    json.put("owner", owner.toString());
    json.put("name", name);
    return json;
  }
}
