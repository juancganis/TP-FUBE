package com.tiendaonline.clientes.service;

import com.statsig.Statsig;
import com.statsig.StatsigUser;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@Slf4j 
public class FeatureFlagService {

    private final ObjectProvider<Statsig> statsigProvider;

    public FeatureFlagService(ObjectProvider<Statsig> statsigProvider) {
        this.statsigProvider = statsigProvider;
    }

    public boolean isEnabled(String flag, String userId) {
        Statsig statsig = statsigProvider.getIfAvailable();
        if (statsig == null) {
            log.debug("Statsig no disponible, el flag {} queda apagado", flag);
            return false; // Statsig apagado (por ejemplo, en tests)
        }
        try {
            StatsigUser user = new StatsigUser.Builder()
                .setUserID(userId)
                .build();
            boolean habilitado = statsig.checkGate(user, flag);
            log.debug("Flag {} evaluado para userId = {}: {}", flag, userId, habilitado);
            return habilitado;
        } catch (Exception e) {
            log.warn("Error al evaluar el flag {} para userId = {}, se usa false por defecto", flag, userId, e);
            return false; // ante cualquier falla, el flag queda apagado
        }
    }
}