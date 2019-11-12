package com.de314.data.micro.controller;

import ch.qos.logback.classic.util.CopyOnInheritThreadLocal;
import com.de314.data.local.api.model.CursorPage;
import com.de314.data.local.api.model.DataRow;
import com.de314.data.local.api.model.KVInfo;
import com.de314.data.local.api.model.ScanOptions;
import com.de314.data.local.api.service.DataStoreFactory;
import com.de314.data.local.disk.RocksKeyValueStore;
import com.de314.data.local.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/data/{namespace}")
public class DataController {

    private final DataStoreFactory storeFactory;
    private final JsonUtils jsonUtils;

    @GetMapping("/count")
    public long count(
            @PathVariable("namespace") String namespace,
            @RequestParam("cursor") Optional<String> cursor,
            @RequestParam("prefix") Optional<String> prefix
    ) {
        RocksKeyValueStore<JsonNode> store = storeFactory.getRocksStore(namespace);
        ScanOptions options = getScanOptions(prefix, cursor).build();
        return store.count(options);
    }

    @GetMapping("/info")
    public KVInfo info(@PathVariable("namespace") String namespace) {
        RocksKeyValueStore<JsonNode> store = storeFactory.getRocksStore(namespace);
        return store.getInfo();
    }


    @GetMapping
    public CursorPage<JsonNode> list(
            @PathVariable("namespace") String namespace,
            @RequestParam("cursor") Optional<String> cursor,
            @RequestParam("prefix") Optional<String> prefix,
            @RequestParam("limit") Optional<Long> limit
    ) {
        RocksKeyValueStore<JsonNode> store = storeFactory.getRocksStore(namespace);
        ScanOptions options = getScanOptions(prefix, cursor)
                .limit(limit.filter(n -> n > 0 && n <= 100).orElse(25L))
                .build();
        return store.scan(options);
    }

    @GetMapping("/{key}")
    public ResponseEntity<JsonNode> details(
            @PathVariable("namespace") String namespace,
            @PathVariable("key") String key
    ) {
        return storeFactory.getRocksStore(namespace).get(key)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> create(
            @PathVariable("namespace") String namespace,
            @RequestBody JsonNode value
    ) {
        String key = UUID.randomUUID().toString();
        storeFactory.getRocksStore(namespace).put(key, value);
        return ResponseEntity.status(HttpStatus.CREATED).body(key);
    }

    @PutMapping("/{key}")
    public ResponseEntity<Void> upsert(
            @PathVariable("namespace") String namespace,
            @PathVariable("key") String key,
            @RequestBody JsonNode value
    ) {
        storeFactory.getRocksStore(namespace).put(key, value);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{key}")
    public ResponseEntity<JsonNode> partialUpdate(
            @PathVariable("namespace") String namespace,
            @PathVariable("key") String key,
            @RequestBody JsonNode value
    ) {
        RocksKeyValueStore<JsonNode> store = storeFactory.getRocksStore(namespace);
        Optional<JsonNode> persisted = store.get(key);
        if (!persisted.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        JsonNode curr = jsonUtils.merge(persisted.get(), value);
        store.put(key, curr);
        return ResponseEntity.ok(curr);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(
            @PathVariable("namespace") String namespace,
            @PathVariable("key") String key
    ) {
        return storeFactory.getRocksStore(namespace).delete(key)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    protected ScanOptions.ScanOptionsBuilder getScanOptions(Optional<String> prefix, Optional<String> cursor) {
        ScanOptions.ScanOptionsBuilder scanOptionsBuilder;
        if (prefix.isPresent()) {
            scanOptionsBuilder = ScanOptions.fromPrefix(prefix.get(), cursor.orElse(null));
        } else if (cursor.isPresent()) {
            scanOptionsBuilder = ScanOptions.fromCursor(cursor.get());
        } else {
            scanOptionsBuilder = ScanOptions.all();
        }
        return scanOptionsBuilder;
    }
}
