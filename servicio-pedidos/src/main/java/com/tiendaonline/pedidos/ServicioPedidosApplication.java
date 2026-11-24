package com.tiendaonline.pedidos;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Activamos el motor de planificación de tareas de Spring (sin esto, se ignoran los metodos anotados
                  // con @Scheduled, entonces nunca se ejecutaría el batch automaticamente).
public class ServicioPedidosApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
        SpringApplication.run(ServicioPedidosApplication.class, args);
    }
}
