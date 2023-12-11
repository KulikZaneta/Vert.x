package com.betacom.vertx_project.repository.impl;

import com.betacom.vertx_project.auth.PasswordHasher;
import com.betacom.vertx_project.repository.UserRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class UserRepositoryImpl implements UserRepository {

  private final MongoClient mongoClient;

  public UserRepositoryImpl(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  @Override
  public void authenticateUser(String login, String password, Handler<AsyncResult<JsonObject>> resultHandler) {
    JsonObject query = new JsonObject().put("login", login);

    mongoClient.findOne("User", query, null, ar -> {
      if (ar.succeeded()) {
        JsonObject user = ar.result();
        if (user != null) {
          String hashedPassword = user.getString("hashedPassword");
          if (PasswordHasher.verifyPassword(password, hashedPassword)) {
            resultHandler.handle(Future.succeededFuture(user));
          } else {
            resultHandler.handle(Future.failedFuture("Invalid credentials"));
          }
        } else {
          resultHandler.handle(Future.failedFuture("User not found"));
        }
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }


  @Override
  public void registerUser(String login, String password, Handler<AsyncResult<Void>> resultHandler) {
    String hashedPassword = PasswordHasher.hashPassword(password);
    JsonObject user = new JsonObject().put("login", login).put("hashedPassword", hashedPassword);

    mongoClient.save("User", user, ar -> {
      if (ar.succeeded()) {
        resultHandler.handle(Future.succeededFuture());
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }
}
