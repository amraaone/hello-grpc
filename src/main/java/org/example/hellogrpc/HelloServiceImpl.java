package org.example.hellogrpc;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class HelloServiceImpl {
    @Value("${grpc.port}")
    private int port;

    private Server server;

    @PostConstruct
    private void startServer() throws IOException {
        server = ServerBuilder.forPort(port).addService(new AppCallback()).build().start();

        System.out.println("Server started, listening on: " + port);
    }

    @PreDestroy
    private void stopServer() {
        if (server != null) {
            System.out.println("Shutting down gRPC Server since JVM is shutting down");
            server.shutdown();
            System.out.println("Server shutdown");
        }
    }
}
