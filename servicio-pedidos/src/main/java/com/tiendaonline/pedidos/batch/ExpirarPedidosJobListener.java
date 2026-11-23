package com.tiendaonline.pedidos.batch;

import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

// Esta clase nos permite enganchar lógica antes y después de la ejecución completa del proceso batch.
@Component // Registramos el listener como bean de Spring para inyectarlo en la configuración del Job.
public class ExpirarPedidosJobListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(ExpirarPedidosJobListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution){

        log.info("Iniciando Job Batch '{}' (Execution ID: {})",
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


        log.info("Finalizó Job Batch '{}' | Estado: {} | Pedidos vencidos leídos: {} | Pedidos expirados guardados: {}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus(),
                totalLeidos,
                totalExpirados);

    }

}
