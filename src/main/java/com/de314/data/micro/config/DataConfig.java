package com.de314.data.micro.config;

import com.de314.data.local.api.kv.KeyValueStore;
import com.de314.data.local.api.service.DataAdapter;
import com.de314.data.local.api.service.DataStoreFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

    @Bean
    public DataStoreFactory dataStoreFactory() {
        return DataStoreFactory.instance();
    }

    @Bean
    public KeyValueStore<String> systemKvStore(DataStoreFactory storeFactory) {
        return storeFactory.getRocksStore("__system", String.class);
    }
}
