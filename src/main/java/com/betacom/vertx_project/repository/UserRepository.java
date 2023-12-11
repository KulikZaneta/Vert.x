package com.betacom.vertx_project.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface UserRepository {

  void authenticateUser(String login, String password, Handler<AsyncResult<JsonObject>> resultHandler);

  void registerUser(String login, String password, Handler<AsyncResult<Void>> resultHandler);
}
