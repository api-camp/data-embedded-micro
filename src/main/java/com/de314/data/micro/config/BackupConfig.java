package com.de314.data.micro.config;

import com.de314.data.local.api.service.SpaceArchiveStrategy;
import com.de314.data.local.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class BackupConfig {

    @Bean
    public ExecutorService backupExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    @Profile("!testing")
    public FileUtils.SpaceConfig spaceConfig(
            @Value("${do.spaces.token}") String accessToken,
            @Value("${do.spaces.secret}") String secret,
            @Value("${do.spaces.endpoint:nyc3.digitaloceanspaces.com}") String serviceEndpoint,
            @Value("${do.spaces.region:nyc3}") String region,
            @Value("${do.spaces.bucket:api-camp-data-backup}") String bucketName
    ) {
        return FileUtils.SpaceConfig.builder()
                .accessToken(accessToken)
                .secret(secret)
                .serviceEndpoint(serviceEndpoint)
                .region(region)
                .bucketName(bucketName)
                .build();
    }

    @Bean
    @Profile("testing")
    public FileUtils.SpaceConfig spaceConfig_testing() {
        return FileUtils.SpaceConfig.builder()
                .accessToken("testToken")
                .secret("testSecret")
                .serviceEndpoint("nyc3.digitaloceanspaces.com")
                .region("nyc3")
                .bucketName("api-camp-data-backup")
                .build();
    }

    @Bean
    public SpaceArchiveStrategy spaceArchiveStrategy(FileUtils.SpaceConfig config) {
        return new SpaceArchiveStrategy(config);
    }
}
