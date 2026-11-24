package com.tiendaonline.pedidos.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

// Esta clase nos permite enganchar lógica antes y después de la ejecución completa del proceso batch.
@Slf4j     // Genera automáticamente en tiempo de compilación un logger SLF4J (atributo 'log'), evitando declararlo manualmente (para no usar LoggerFactory).
@Component // Registramos el listener como bean de Spring para inyectarlo en la configuración del Job.
public class ExpirarPedidosJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution){

        // Creamos un log que avisa el comienzo del Job Batch (cuando sea que esté programado, indicando su ID).
        log.info("Iniciando el Job Batch '{}' (ID de ejecución: {}).",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getId());

    }

    @Override
    public void afterJob(JobExecution jobExecution){

        // Recorremos las métricas del Step ejecutado para obtener valores totales reales de lectura y escritura:
        long totalLeidos = jobExecution.getStepExecutions().stream()
                .mapToLong(StepExecution::getReadCount)
                .sum();

        long totalExpirados = jobExecution.getStepExecutions().stream()
                        .mapToLong(StepExecution::getWriteCount)
                                .sum();


        // Creamos un log que avisa de la finalización del Job Batch, mostrando datos de nombre, estado, cant. de pedidos vencidos leídos y los expirados guardados.
        log.info("Finalizó el Job Batch '{}' / Estado: {} / Pedidos vencidos leídos: {} / Pedidos expirados guardados: {}.",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus(),
                totalLeidos,
                totalExpirados);

    }

}