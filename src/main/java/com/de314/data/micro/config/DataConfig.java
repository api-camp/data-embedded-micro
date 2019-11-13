package com.de314.data.micro.config;

import com.de314.data.local.api.kv.KeyValueStore;
import com.de314.data.local.disk.RockDBDataStoreService;
import com.de314.data.local.memory.MemoryMapDataStoreService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

    @Bean
    public RockDBDataStoreService rocksDataStoreService() {
        return (RockDBDataStoreService) RockDBDataStoreService.instance();
    }

    @Bean
    public MemoryMapDataStoreService memoryMapDataStoreService() {
        return (MemoryMapDataStoreService) MemoryMapDataStoreService.instance();
    }

    @Bean
    public KeyValueStore<String> systemKvStore(MemoryMapDataStoreService memoryMapDataStoreService) {
        return memoryMapDataStoreService.getOrCreate("__system", String.class);
    }
}
