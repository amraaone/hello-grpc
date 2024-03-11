package org.example.hellogrpc;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloService {

  private static final Logger logger = LoggerFactory.getLogger(
    HelloService.class
  );
  private Server server;

  public void start(int port) throws IOException {
    server =
      Grpc
        .newServerBuilderForPort(port, InsecureServerCredentials.create())
        .addService(new HelloServiceImpl())
        .maxInboundMessageSize(100 * 1024 * 1024)
        .build()
        .start();

    logger.info("Server started, listening on " + port);

    Runtime
      .getRuntime()
      .addShutdownHook(
        new Thread(() -> {
          logger.error(
            "*** shutting down gRPC server since JVM is shutting down"
          );
          try {
            HelloService.this.stop();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          logger.error("*** server shut down");
        })
      );
  }

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
      logger.info("Greet to " + req.getName());
      HelloReply reply = HelloReply
        .newBuilder()
        .setMessage("Hello " + req.getName())
        .build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
  }
}
