package com.activity.squad2.secrets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultSecretProvider implements SecretProvider {
    @Value("${icmap.api.key:17d41dba-fa77-11e6-a437-0b873af2b3d1}")
    private String icmapApiKey;

    @Override
    public String getICMAPApiKey() {
        return icmapApiKey;
    }
}