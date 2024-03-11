package org.example.hellogrpc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.Any;
import io.dapr.v1.AppCallbackGrpc.AppCallbackImplBase;
import io.dapr.v1.CommonProtos.InvokeRequest;
import io.dapr.v1.CommonProtos.InvokeResponse;
import io.grpc.stub.StreamObserver;

public class AppCallback extends AppCallbackImplBase {

  private static final Gson gson = new Gson();

  @Override
  public void onInvoke(
    InvokeRequest request,
    StreamObserver<InvokeResponse> responseObserver
  ) {
    try {
      if ("sayHello".equals(request.getMethod())) {
        String requestData = request.getData().getValue().toStringUtf8();
        JsonObject jsonObject = gson.fromJson(requestData, JsonObject.class);
        String name = jsonObject.has("name")
          ? jsonObject.get("name").getAsString()
          : "unknown";

        // byte[] requestBytes = request.getData().getValue().toByteArray();
        // HelloRequest helloRequest = HelloRequest.parseFrom(requestBytes);

        HelloReply helloReply = HelloReply
          .newBuilder()
          .setMessage("Hello" + "enebibn")
          .build();

        InvokeResponse response = InvokeResponse
          .newBuilder()
          .setData(Any.pack(helloReply))
          .build();

        responseObserver.onNext(response);
      }
    } catch (Exception e) {
      e.printStackTrace();
      responseObserver.onError(e);
    } finally {
      responseObserver.onCompleted();
    }
  }
}
