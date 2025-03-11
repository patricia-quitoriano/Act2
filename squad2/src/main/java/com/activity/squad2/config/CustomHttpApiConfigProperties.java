package com.activity.squad2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import com.bpi.framework.web.configproperties.HttpApiConfigProperties;

@ConfigurationProperties(prefix = "icmap.api")
public class CustomHttpApiConfigProperties extends HttpApiConfigProperties {
    // Default implementation
    private String basePath = "http://localhost:8080";
    private boolean backendAdapterEnabled = false;
    private boolean proxyEnabled = false;
    private long defaultKeepAlive = 5000L;
    private long connectionRequestTimeout = 1000L;
    private int connectTimeout = 1000;
    private int socketTimeout = 3000;

    // Getters and setters
    @Override
    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public boolean isBackendAdapterEnabled() {
        return backendAdapterEnabled;
    }

    public void setBackendAdapterEnabled(boolean backendAdapterEnabled) {
        this.backendAdapterEnabled = backendAdapterEnabled;
    }

    @Override
    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    @Override
    public long getDefaultKeepAlive() {
        return defaultKeepAlive;
    }

    public void setDefaultKeepAlive(long defaultKeepAlive) {
        this.defaultKeepAlive = defaultKeepAlive;
    }

    @Override
    public long getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(long connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    @Override
    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Override
    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}