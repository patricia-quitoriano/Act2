package com.activity.squad2.config;

import com.bpi.framework.web.configproperties.HttpApiConfigProperties;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestHttpApiConfig {

    @Bean
    @Primary
    public HttpApiConfigProperties httpApiConfigProperties() {
        return new TestHttpApiConfigProperties();
    }

    @Bean
    public PoolingHttpClientConnectionManager httpClientConnectionManager() {
        return new PoolingHttpClientConnectionManager();
    }

    // Implement the interface correctly
    public static class TestHttpApiConfigProperties extends HttpApiConfigProperties {
        @Override
        public String getBasePath() { return "http://localhost:3001"; }

        @Override
        public boolean isBackendAdapterEnabled() { return false; }

        @Override
        public boolean isProxyEnabled() { return false; }

        @Override
        public long getDefaultKeepAlive() { return 5000; }

        @Override
        public long getConnectionRequestTimeout() { return 1000; }

        @Override
        public int getConnectTimeout() { return 1000; }

        @Override
        public int getSocketTimeout() { return 3000; }
    }
}