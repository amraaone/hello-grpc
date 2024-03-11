package org.example.hellogrpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloGrpcApplication implements CommandLineRunner {

  @Autowired
  private HelloService helloService;

  public static void main(String[] args) {
    SpringApplication.run(HelloGrpcApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    int port = 50051;
    helloService.start(port);
    helloService.blockUntilShutdown();
  }
}
