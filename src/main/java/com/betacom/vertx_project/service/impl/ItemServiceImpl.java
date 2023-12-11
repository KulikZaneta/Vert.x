package com.betacom.vertx_project.service.impl;

import com.betacom.vertx_project.repository.ItemRepository;
import com.betacom.vertx_project.service.ItemService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.UUID;

public class ItemServiceImpl implements ItemService {

  private final ItemRepository itemRepository;

  public ItemServiceImpl(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  @Override
  public void getItems(UUID ownerId, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
    itemRepository.getItems(ownerId, resultHandler);
  }

  @Override
  public void addItem(UUID ownerId, JsonObject itemJson, Handler<AsyncResult<Void>> resultHandler) {
    itemRepository.addItem(ownerId, itemJson, resultHandler);
  }
}
