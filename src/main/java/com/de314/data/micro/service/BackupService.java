package com.de314.data.micro.service;

import com.de314.data.local.api.service.SpaceArchiveStrategy;
import com.de314.data.local.disk.RockDBDataStoreService;
import com.de314.data.local.utils.FileUtils;
import com.de314.data.local.utils.metrics.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupService {

    private final RockDBDataStoreService rocksService;
    private final SpaceArchiveStrategy archiveStrategy;
    private final ExecutorService backupExecutorService;

    @Value("${do.spaces.accessKey:UNKNOWN}")
    private String doSpaceAccessKey;
    @Value("${do.spaces.secret:UNKNOWN}")
    private String doSpaceSecret;

    public boolean backup(final String namespace) {
        return rocksService.get(namespace)
                .map(store -> {
                    backupExecutorService.submit(
                            () -> rocksService.backup(namespace, archiveStrategy)
                    );
                    return true;
                })
                .orElse(false);
    }

    public boolean revert(final String namespace) {
        return rocksService.get(namespace)
                .map(store -> {
                    backupExecutorService.submit(
                            () -> rocksService.rollback(namespace, archiveStrategy)
                    );
                    return true;
                })
                .orElse(false);
    }
}
