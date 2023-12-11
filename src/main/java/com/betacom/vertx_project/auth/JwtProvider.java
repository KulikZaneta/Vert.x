package com.betacom.vertx_project.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.JWTOptions;

public class JwtProvider {

  private final JWTAuth jwtAuth;

  public JwtProvider(Vertx vertx, String secretKey) {
    this.jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm("HS256")
        .setSecretKey(secretKey)
        .setSymmetric(true)));
  }

  public boolean validateToken(String token) {
    try {
      // Weryfikacja tokena bez dekodowania
      return jwtAuth.authenticate(new JsonObject().put("jwt", token)).succeeded();
    } catch (Exception e) {
      return false;
    }
  }

  public void getLoginFromToken(String token, Handler<AsyncResult<String>> resultHandler) {
    jwtAuth.authenticate(new JsonObject().put("jwt", token), res -> {
      if (res.succeeded()) {
        User authenticatedUser = res.result();
        String login = authenticatedUser.principal().getString("sub");
        resultHandler.handle(io.vertx.core.Future.succeededFuture(login));
      } else {
        resultHandler.handle(io.vertx.core.Future.failedFuture(res.cause()));
      }
    });
  }

  public JWTAuth getJwtAuth() {
    return jwtAuth;
  }
}
