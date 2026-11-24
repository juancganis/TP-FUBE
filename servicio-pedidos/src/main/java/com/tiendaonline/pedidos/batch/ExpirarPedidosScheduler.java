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

    // Programamos la ejecución automática del job según la expresión cron que definimos en application.yml (02:00 AM).
    @Scheduled(cron = "${batch.expiration.cron}")
    public void ejecutarExpiracionPedidos() {

        try {

            JobParameters params = new JobParametersBuilder()
                    // Pasamos el tiempo actual como JobParameter porque Spring Batch nos exige parámetros únicos para iniciar una nueva instancia del Job.
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(expirarPedidosJob, params);

        } catch (Exception e) {
            // Creamos un log del tipo ERROR para indicar que hubo un error al ejecutar el proceso batch.
            log.error("Error al ejecutar el proceso batch expirarPedidosJob: {}.", e.getMessage(), e);
        }

    }

}
