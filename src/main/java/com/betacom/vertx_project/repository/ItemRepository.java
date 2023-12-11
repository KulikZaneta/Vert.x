package com.betacom.vertx_project.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.UUID;

public interface ItemRepository {

  void getItems(UUID ownerId, Handler<AsyncResult<List<JsonObject>>> resultHandler);

  void addItem(UUID ownerId, JsonObject itemJson, Handler<AsyncResult<Void>> resultHandler);
}
