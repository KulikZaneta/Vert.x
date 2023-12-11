package com.betacom.vertx_project.service.impl;

import com.betacom.vertx_project.auth.PasswordHasher;
import com.betacom.vertx_project.repository.UserRepository;
import com.betacom.vertx_project.service.UserService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;

public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final JWTAuth jwtAuth;

  public UserServiceImpl(UserRepository userRepository, JWTAuth jwtAuth) {
    this.userRepository = userRepository;
    this.jwtAuth = jwtAuth;
  }

  @Override
  public void registerUser(String login, String password, Handler<AsyncResult<Void>> resultHandler) {
    if (!isValidPassword(password)) {
      resultHandler.handle(Future.failedFuture("Invalid password"));
      return;
    }

    String hashedPassword = PasswordHasher.hashPassword(password);

    userRepository.registerUser(login, hashedPassword, ar -> {
      if (ar.succeeded()) {
        resultHandler.handle(Future.succeededFuture());
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }

  private boolean isValidPassword(String password) {
    // Sprawdzenie, czy hasło ma co najmniej 8 znaków, zawiera co najmniej jedną cyfrę i jeden znak specjalny
    return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
  }

  @Override
  public void loginUser(String login, String password, Handler<AsyncResult<String>> resultHandler) {
    userRepository.authenticateUser(login, password, authResult -> {
      if (authResult.succeeded()) {
        String token = jwtAuth.generateToken(new JsonObject().put("sub", login));
        resultHandler.handle(Future.succeededFuture(token));
      } else {
        resultHandler.handle(Future.failedFuture("Invalid credentials"));
      }
    });
  }
}
