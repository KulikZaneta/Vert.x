package com.betacom.vertx_project.repository.impl;

import com.betacom.vertx_project.repository.ItemRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;
import java.util.UUID;

public class ItemRepositoryImpl implements ItemRepository {

  private final MongoClient mongoClient;

  public ItemRepositoryImpl(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  @Override
  public void getItems(UUID ownerId, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
    JsonObject query = new JsonObject().put("ownerId", ownerId.toString());

    mongoClient.find("Item", query, res -> {
      if (res.succeeded()) {
        List<JsonObject> items = res.result();
        resultHandler.handle(Future.succeededFuture(items));
      } else {
        resultHandler.handle(Future.failedFuture(res.cause()));
      }
    });
  }

  @Override
  public void addItem(UUID ownerId, JsonObject itemJson, Handler<AsyncResult<Void>> resultHandler) {
    itemJson.put("ownerId", ownerId.toString());

    mongoClient.save("Item", itemJson, ar -> {
      if (ar.succeeded()) {
        resultHandler.handle(Future.succeededFuture());
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }
}
