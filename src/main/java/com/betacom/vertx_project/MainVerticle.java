package com.betacom.vertx_project;

import com.betacom.vertx_project.auth.JwtProvider;
import com.betacom.vertx_project.repository.impl.ItemRepositoryImpl;
import com.betacom.vertx_project.repository.impl.UserRepositoryImpl;
import com.betacom.vertx_project.server.HttpServer;
import com.betacom.vertx_project.service.ItemService;
import com.betacom.vertx_project.service.UserService;
import com.betacom.vertx_project.service.impl.ItemServiceImpl;
import com.betacom.vertx_project.service.impl.UserServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    try {
      JsonObject config = new JsonObject()
        .put("connection_string", "mongodb://localhost:27017")
        .put("db_name", "betacom");

      MongoClient mongoClient = MongoClient.createShared(vertx, config);

      JwtProvider jwtProvider = new JwtProvider(vertx, "secretKey");
      JWTAuth jwtAuth = jwtProvider.getJwtAuth();
      UserService userService = new UserServiceImpl(new UserRepositoryImpl(mongoClient), jwtAuth);
      ItemService itemService = new ItemServiceImpl(new ItemRepositoryImpl(mongoClient));

      HttpServer httpServer = new HttpServer(vertx, userService, itemService, jwtProvider);

      vertx.createHttpServer()
        .requestHandler(httpServer.getRouter())
        .listen(3000, "localhost", http -> {
          if (http.succeeded()) {
            startPromise.complete();
            System.out.println("HTTP server started on port 3000");
          } else {
            startPromise.fail(http.cause());
          }
        });
    } catch (Exception e) {
      startPromise.fail(e);
    }
  }
}
