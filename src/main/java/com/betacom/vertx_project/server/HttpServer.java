package com.betacom.vertx_project.server;

import com.betacom.vertx_project.auth.JwtProvider;
import com.betacom.vertx_project.service.ItemService;
import com.betacom.vertx_project.service.UserService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.List;
import java.util.UUID;

public class HttpServer {

  private final UserService userService;
  private final ItemService itemService;
  private final JwtProvider jwtProvider;
  private final Router router;

  public HttpServer(Vertx vertx, UserService userService, ItemService itemService, JwtProvider jwtProvider) {
    this.userService = userService;
    this.itemService = itemService;
    this.jwtProvider = jwtProvider;
    this.router = Router.router(vertx);
    initializeRoutes();
  }

  private void initializeRoutes() {
    router.post("/login").handler(ctx -> {
      JsonObject requestJson = ctx.getBodyAsJson();
      String login = requestJson.getString("login");
      String password = requestJson.getString("password");

      userService.loginUser(login, password, loginResult -> {
        if (loginResult.succeeded()) {
          String token = loginResult.result();
          ctx.response().putHeader("Content-Type", "application/json").end(new JsonObject().put("token", token).encode());
        } else {
          ctx.response().setStatusCode(401).end("Invalid credentials");
        }
      });
    });

    router.post("/register").handler(ctx -> {
      JsonObject requestJson = ctx.getBodyAsJson();
      String login = requestJson.getString("login");
      String password = requestJson.getString("password");

      userService.registerUser(login, password, registerResult -> {
        if (registerResult.succeeded()) {
          ctx.response().setStatusCode(201).end("User registered successfully");
        } else {
          ctx.response().setStatusCode(400).end("Failed to register user");
        }
      });
    });

    router.post("/items").handler(ctx -> {
      String token = ctx.request().getHeader(HttpHeaders.AUTHORIZATION);
      if (token == null || !jwtProvider.validateToken(token)) {
        ctx.response().setStatusCode(401).end("Unauthorized");
        return;
      }

      jwtProvider.getLoginFromToken(token, loginResult -> {
        if (loginResult.succeeded()) {
          String login = loginResult.result();
          JsonObject requestJson = ctx.getBodyAsJson();
          String itemName = requestJson.getString("title");
          UUID ownerId = UUID.fromString(login);

          itemService.addItem(ownerId, JsonObject.mapFrom(itemName), addItemResult -> {
            if (addItemResult.succeeded()) {
              ctx.response().setStatusCode(204).end("Item created successfully");
            } else {
              ctx.response().setStatusCode(500).end("Failed to create item");
            }
          });
        } else {
          ctx.response().setStatusCode(401).end("Invalid or expired token");
        }
      });
    });

    router.get("/items").handler(ctx -> {
      String token = ctx.request().getHeader(HttpHeaders.AUTHORIZATION);
      if (token == null || !jwtProvider.validateToken(token)) {
        ctx.response().setStatusCode(401).end("Unauthorized");
        return;
      }

      jwtProvider.getLoginFromToken(token, loginResult -> {
        if (loginResult.succeeded()) {
          String login = loginResult.result();
          UUID ownerId = UUID.fromString(login);
          itemService.getItems(ownerId, getItemsResult -> {
            if (getItemsResult.succeeded()) {
              List<JsonObject> items = getItemsResult.result();
              JsonArray jsonArray = new JsonArray(items);

              ctx.response().putHeader("Content-Type", "application/json").end(jsonArray.encode());
            } else {
              ctx.response().setStatusCode(500).end("Failed to retrieve items");
            }
          });

        } else {
          ctx.response().setStatusCode(401).end("Invalid or expired token");
        }
      });
    });
  }

  public Router getRouter() {
    return router;
  }
}
