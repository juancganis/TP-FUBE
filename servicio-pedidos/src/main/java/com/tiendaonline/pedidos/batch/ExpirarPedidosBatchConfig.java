package com.tiendaonline.pedidos.batch;

import com.tiendaonline.pedidos.enums.EstadoPedido;
import com.tiendaonline.pedidos.models.Pedido;
import com.tiendaonline.pedidos.repository.PedidoRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Configuration
public class ExpirarPedidosBatchConfig {

    @Bean
    @StepScope
    // Con @StepScope retrasamos la creación del reader hasta el momento exacto en que arranca cada ejecución del step,
    // a fin de que LocalDateTime.now().minusHours(24) se calcule nuevo en cada corrida y no quede congelado al arrancar el servicio.
    public RepositoryItemReader<Pedido> pedidosVencidosReader(PedidoRepository pedidoRepository) {

        // Tomamos la fecha/hora actual y le restamos 24 horas para obtener la fecha límite;
        // todos los pedidos cuya fechaCreacion sean anteriores (Before) a ese instante tienen más de 24 horas y deben expirar.
        LocalDateTime fechaLimite = LocalDateTime.now().minusHours(24);

        return new RepositoryItemReaderBuilder<Pedido>()
                .name("pedidosVencidosReader")
                .repository(pedidoRepository)
                .methodName("findByEstadoAndFechaCreacionBefore") // Nombre exacto del metodo definido en PedidoRepository.
                .arguments(List.of(EstadoPedido.CREADO, fechaLimite)) // Pasamos los parámetros de filtro (estado y fecha límite) al metodo del repositorio.
                .pageSize(10) // Esta es la cantidad de registros que trae cada consulta paginada a la base de datos (mismo tamaño que el chunk).
                .sorts(Map.of("id", Sort.Direction.ASC)) // Seteamos un orden para evitar saltos o duplicados al paginar entre chunks.
                .build();
    }

    // Con este metodo persistimos cada chunk procesado.
    @Bean
    public RepositoryItemWriter<Pedido> pedidosExpiradosWriter(PedidoRepository pedidoRepository) {

        return new RepositoryItemWriterBuilder<Pedido>()
                .repository(pedidoRepository)
                .methodName("save")
                .build();

    }

    // Con este metodo definimos el Step quee lee, procesa y confirma transacciones de a 10 pedidos por chunk.
    @Bean
    public Step expirarPedidosStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   RepositoryItemReader<Pedido> pedidosVencidosReader,
                                   PedidoExpiradoProcessor pedidoExpiradoProcessor,
                                   RepositoryItemWriter<Pedido> pedidosExpiradosWriter) {

        return new StepBuilder("expirarPedidosStep", jobRepository)
                .<Pedido, Pedido>chunk(10, transactionManager)
                .reader(pedidosVencidosReader)
                .processor(pedidoExpiradoProcessor)
                .writer(pedidosExpiradosWriter)
                .build();

    }

    // Con este metodo definimos el Job principal compuesto por el Step y el Listener.
    @Bean
    public Job expirarPedidosJob(JobRepository jobRepository,
                                 Step expirarPedidosStep,
                                 ExpirarPedidosJobListener listener) {

        return new JobBuilder("expirarPedidosJob", jobRepository)
                .listener(listener)
                .start(expirarPedidosStep)
                .build();

    }

}