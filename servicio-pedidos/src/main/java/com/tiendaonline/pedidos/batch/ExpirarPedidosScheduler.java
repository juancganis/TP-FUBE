package com.tiendaonline.pedidos.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExpirarPedidosScheduler {

    private final JobLauncher jobLauncher;
    private final Job expirarPedidosJob;

    // Inyección de dependencias:
    public ExpirarPedidosScheduler(JobLauncher jobLauncher, Job expirarPedidosJob) {
        this.jobLauncher = jobLauncher;
        this.expirarPedidosJob = expirarPedidosJob;
    }

    // Se ejecuta automáticamente cada 1 hora (convención tomada para que los pedidos
    // vencidos liberen su stock lo antes posible sin sobrecargar la base de datos).
    @Scheduled(fixedRate = 3600000) // La tasa fija de tiempo se mide en ms → 1 hora = 3.600.000 ms.
    public void ejecutarExpiracionPedidos() {

        try {

            JobParameters params = new JobParametersBuilder()
                    // Pasamos el tiempo actual como JobParameter porque Spring Batch nos exige parámetros únicos para iniciar una nueva instancia del Job.
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(expirarPedidosJob, params);

        } catch (Exception e) {
            log.error("Error al ejecutar el proceso batch expirarPedidosJob: {}", e.getMessage(), e);
        }

    }

}
