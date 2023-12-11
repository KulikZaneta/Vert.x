package com.betacom.vertx_project.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface UserService {

  void registerUser(String login, String password, Handler<AsyncResult<Void>> resultHandler);

  void loginUser(String login, String password, Handler<AsyncResult<String>> resultHandler);
}
