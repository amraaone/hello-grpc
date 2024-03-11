package org.example.hellogrpc;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

  private Server server;

  @Value("${grpc.port}")
  private int port;

  @PostConstruct
  public void start() throws IOException {
    server =
      Grpc
        .newServerBuilderForPort(port, InsecureServerCredentials.create())
        .addService(new HelloServiceImpl())
        .maxInboundMessageSize(100 * 1024 * 1024)
        .build()
        .start();

    Runtime
      .getRuntime()
      .addShutdownHook(
        new Thread(() -> {
          try {
            HelloService.this.stop();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        })
      );
  }

  @PreDestroy
  public void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown().awaitTermination();
    }
  }

  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  static class HelloServiceImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(
      HelloRequest req,
      StreamObserver<HelloReply> responseObserver
    ) {
      HelloReply reply = HelloReply
        .newBuilder()
        .setMessage("Hello " + req.getName())
        .build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
  }
}
