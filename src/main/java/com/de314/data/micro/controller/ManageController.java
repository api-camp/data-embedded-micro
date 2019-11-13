package com.de314.data.micro.controller;

import com.de314.data.local.api.model.KVInfo;
import com.de314.data.local.api.model.ScanOptions;
import com.de314.data.local.api.service.DataStoreService;
import com.de314.data.local.disk.RockDBDataStoreService;
import com.de314.data.micro.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ManageController {

    private final RockDBDataStoreService storeService;
    private final BackupService backupService;

    @GetMapping("/data/count/{namespace}")
    public long count(
            @PathVariable("namespace") String namespace,
            @RequestParam("cursor") Optional<String> cursor,
            @RequestParam("prefix") Optional<String> prefix
    ) {
        return storeService.get(namespace)
                .map(store -> store.count(ScanOptions.from(prefix, cursor).build()))
                .orElse(0L);
    }

    @GetMapping("/data/info")
    public List<KVInfo> entries() {
        return storeService.entries();
    }

    @GetMapping("/data/info/{namespace}")
    public ResponseEntity<KVInfo> info(@PathVariable("namespace") String namespace) {
        return storeService.get(namespace)
                .map(store -> ResponseEntity.ok(store.getInfo()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Value("${do.spaces.accessKey:UNKNOWN}")
    private String doSpaceAccessKey;
    @Value("${do.spaces.secret:UNKNOWN}")
    private String doSpaceSecret;

    @PostMapping("/data/backup/{namespace}")
    public ResponseEntity<Boolean> backup(@PathVariable("namespace") String namespace) {
        return backupService.backup(namespace)
                ? ResponseEntity.ok(true)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/data/revert/{namespace}")
    public ResponseEntity<Boolean> revert(@PathVariable("namespace") String namespace) {
        return backupService.revert(namespace)
                ? ResponseEntity.ok(true)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/data/destroy/{namespace}")
    public ResponseEntity<Boolean> destroy(@PathVariable("namespace") String namespace) {
        return storeService.get(namespace)
                .map(store -> {
                    store.delete(ScanOptions.all().build());
//                    storeFactory.hideRocksNamespace(namespace);
                    return ResponseEntity.ok(true);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
