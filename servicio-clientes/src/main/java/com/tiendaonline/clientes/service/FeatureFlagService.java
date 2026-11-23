package com.tiendaonline.clientes.service;

import com.statsig.Statsig;
import com.statsig.StatsigUser;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class FeatureFlagService {

    private final ObjectProvider<Statsig> statsigProvider;

    public FeatureFlagService(ObjectProvider<Statsig> statsigProvider) {
        this.statsigProvider = statsigProvider;
    }

    public boolean isEnabled(String flag, String userId) {
        Statsig statsig = statsigProvider.getIfAvailable();
        if (statsig == null) {
            return false; // Statsig apagado (por ejemplo, en tests)
        }
        try {
            StatsigUser user = new StatsigUser.Builder()
                .setUserID(userId)
                .build();
            return statsig.checkGate(user, flag);
        } catch (Exception e) {
            return false; // ante cualquier falla, el flag queda apagado
        }
    }
}