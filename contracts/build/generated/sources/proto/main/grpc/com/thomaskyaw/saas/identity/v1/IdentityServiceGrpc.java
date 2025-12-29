package com.thomaskyaw.saas.identity.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Service definition for Identity operations
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.69.0)",
    comments = "Source: identity/v1/identity.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class IdentityServiceGrpc {

  private IdentityServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "identity.v1.IdentityService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest,
      com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse> getVerifyCredentialsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerifyCredentials",
      requestType = com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest.class,
      responseType = com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest,
      com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse> getVerifyCredentialsMethod() {
    io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest, com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse> getVerifyCredentialsMethod;
    if ((getVerifyCredentialsMethod = IdentityServiceGrpc.getVerifyCredentialsMethod) == null) {
      synchronized (IdentityServiceGrpc.class) {
        if ((getVerifyCredentialsMethod = IdentityServiceGrpc.getVerifyCredentialsMethod) == null) {
          IdentityServiceGrpc.getVerifyCredentialsMethod = getVerifyCredentialsMethod =
              io.grpc.MethodDescriptor.<com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest, com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "VerifyCredentials"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new IdentityServiceMethodDescriptorSupplier("VerifyCredentials"))
              .build();
        }
      }
    }
    return getVerifyCredentialsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.GetUserRequest,
      com.thomaskyaw.saas.identity.v1.GetUserResponse> getGetUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUser",
      requestType = com.thomaskyaw.saas.identity.v1.GetUserRequest.class,
      responseType = com.thomaskyaw.saas.identity.v1.GetUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.GetUserRequest,
      com.thomaskyaw.saas.identity.v1.GetUserResponse> getGetUserMethod() {
    io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.GetUserRequest, com.thomaskyaw.saas.identity.v1.GetUserResponse> getGetUserMethod;
    if ((getGetUserMethod = IdentityServiceGrpc.getGetUserMethod) == null) {
      synchronized (IdentityServiceGrpc.class) {
        if ((getGetUserMethod = IdentityServiceGrpc.getGetUserMethod) == null) {
          IdentityServiceGrpc.getGetUserMethod = getGetUserMethod =
              io.grpc.MethodDescriptor.<com.thomaskyaw.saas.identity.v1.GetUserRequest, com.thomaskyaw.saas.identity.v1.GetUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.thomaskyaw.saas.identity.v1.GetUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.thomaskyaw.saas.identity.v1.GetUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new IdentityServiceMethodDescriptorSupplier("GetUser"))
              .build();
        }
      }
    }
    return getGetUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest,
      com.thomaskyaw.saas.identity.v1.GetUserResponse> getGetUserByEmailMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUserByEmail",
      requestType = com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest.class,
      responseType = com.thomaskyaw.saas.identity.v1.GetUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest,
      com.thomaskyaw.saas.identity.v1.GetUserResponse> getGetUserByEmailMethod() {
    io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest, com.thomaskyaw.saas.identity.v1.GetUserResponse> getGetUserByEmailMethod;
    if ((getGetUserByEmailMethod = IdentityServiceGrpc.getGetUserByEmailMethod) == null) {
      synchronized (IdentityServiceGrpc.class) {
        if ((getGetUserByEmailMethod = IdentityServiceGrpc.getGetUserByEmailMethod) == null) {
          IdentityServiceGrpc.getGetUserByEmailMethod = getGetUserByEmailMethod =
              io.grpc.MethodDescriptor.<com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest, com.thomaskyaw.saas.identity.v1.GetUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUserByEmail"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.thomaskyaw.saas.identity.v1.GetUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new IdentityServiceMethodDescriptorSupplier("GetUserByEmail"))
              .build();
        }
      }
    }
    return getGetUserByEmailMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.CreateUserRequest,
      com.thomaskyaw.saas.identity.v1.CreateUserResponse> getCreateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateUser",
      requestType = com.thomaskyaw.saas.identity.v1.CreateUserRequest.class,
      responseType = com.thomaskyaw.saas.identity.v1.CreateUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.CreateUserRequest,
      com.thomaskyaw.saas.identity.v1.CreateUserResponse> getCreateUserMethod() {
    io.grpc.MethodDescriptor<com.thomaskyaw.saas.identity.v1.CreateUserRequest, com.thomaskyaw.saas.identity.v1.CreateUserResponse> getCreateUserMethod;
    if ((getCreateUserMethod = IdentityServiceGrpc.getCreateUserMethod) == null) {
      synchronized (IdentityServiceGrpc.class) {
        if ((getCreateUserMethod = IdentityServiceGrpc.getCreateUserMethod) == null) {
          IdentityServiceGrpc.getCreateUserMethod = getCreateUserMethod =
              io.grpc.MethodDescriptor.<com.thomaskyaw.saas.identity.v1.CreateUserRequest, com.thomaskyaw.saas.identity.v1.CreateUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.thomaskyaw.saas.identity.v1.CreateUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.thomaskyaw.saas.identity.v1.CreateUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new IdentityServiceMethodDescriptorSupplier("CreateUser"))
              .build();
        }
      }
    }
    return getCreateUserMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static IdentityServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<IdentityServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<IdentityServiceStub>() {
        @java.lang.Override
        public IdentityServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new IdentityServiceStub(channel, callOptions);
        }
      };
    return IdentityServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static IdentityServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<IdentityServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<IdentityServiceBlockingStub>() {
        @java.lang.Override
        public IdentityServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new IdentityServiceBlockingStub(channel, callOptions);
        }
      };
    return IdentityServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static IdentityServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<IdentityServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<IdentityServiceFutureStub>() {
        @java.lang.Override
        public IdentityServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new IdentityServiceFutureStub(channel, callOptions);
        }
      };
    return IdentityServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Service definition for Identity operations
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Verifies user credentials (used by Auth Service)
     * </pre>
     */
    default void verifyCredentials(com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest request,
        io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVerifyCredentialsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Gets user details by ID
     * </pre>
     */
    default void getUser(com.thomaskyaw.saas.identity.v1.GetUserRequest request,
        io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.GetUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserMethod(), responseObserver);
    }

    /**
     * <pre>
     * Gets user details by Email
     * </pre>
     */
    default void getUserByEmail(com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest request,
        io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.GetUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserByEmailMethod(), responseObserver);
    }

    /**
     * <pre>
     * Creates a new user
     * </pre>
     */
    default void createUser(com.thomaskyaw.saas.identity.v1.CreateUserRequest request,
        io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.CreateUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateUserMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service IdentityService.
   * <pre>
   * Service definition for Identity operations
   * </pre>
   */
  public static abstract class IdentityServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return IdentityServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service IdentityService.
   * <pre>
   * Service definition for Identity operations
   * </pre>
   */
  public static final class IdentityServiceStub
      extends io.grpc.stub.AbstractAsyncStub<IdentityServiceStub> {
    private IdentityServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IdentityServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new IdentityServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Verifies user credentials (used by Auth Service)
     * </pre>
     */
    public void verifyCredentials(com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest request,
        io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVerifyCredentialsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Gets user details by ID
     * </pre>
     */
    public void getUser(com.thomaskyaw.saas.identity.v1.GetUserRequest request,
        io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.GetUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Gets user details by Email
     * </pre>
     */
    public void getUserByEmail(com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest request,
        io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.GetUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserByEmailMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Creates a new user
     * </pre>
     */
    public void createUser(com.thomaskyaw.saas.identity.v1.CreateUserRequest request,
        io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.CreateUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service IdentityService.
   * <pre>
   * Service definition for Identity operations
   * </pre>
   */
  public static final class IdentityServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<IdentityServiceBlockingStub> {
    private IdentityServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IdentityServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new IdentityServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Verifies user credentials (used by Auth Service)
     * </pre>
     */
    public com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse verifyCredentials(com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVerifyCredentialsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Gets user details by ID
     * </pre>
     */
    public com.thomaskyaw.saas.identity.v1.GetUserResponse getUser(com.thomaskyaw.saas.identity.v1.GetUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Gets user details by Email
     * </pre>
     */
    public com.thomaskyaw.saas.identity.v1.GetUserResponse getUserByEmail(com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserByEmailMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Creates a new user
     * </pre>
     */
    public com.thomaskyaw.saas.identity.v1.CreateUserResponse createUser(com.thomaskyaw.saas.identity.v1.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateUserMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service IdentityService.
   * <pre>
   * Service definition for Identity operations
   * </pre>
   */
  public static final class IdentityServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<IdentityServiceFutureStub> {
    private IdentityServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IdentityServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new IdentityServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Verifies user credentials (used by Auth Service)
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse> verifyCredentials(
        com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVerifyCredentialsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Gets user details by ID
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.thomaskyaw.saas.identity.v1.GetUserResponse> getUser(
        com.thomaskyaw.saas.identity.v1.GetUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Gets user details by Email
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.thomaskyaw.saas.identity.v1.GetUserResponse> getUserByEmail(
        com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserByEmailMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Creates a new user
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.thomaskyaw.saas.identity.v1.CreateUserResponse> createUser(
        com.thomaskyaw.saas.identity.v1.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VERIFY_CREDENTIALS = 0;
  private static final int METHODID_GET_USER = 1;
  private static final int METHODID_GET_USER_BY_EMAIL = 2;
  private static final int METHODID_CREATE_USER = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_VERIFY_CREDENTIALS:
          serviceImpl.verifyCredentials((com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest) request,
              (io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse>) responseObserver);
          break;
        case METHODID_GET_USER:
          serviceImpl.getUser((com.thomaskyaw.saas.identity.v1.GetUserRequest) request,
              (io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.GetUserResponse>) responseObserver);
          break;
        case METHODID_GET_USER_BY_EMAIL:
          serviceImpl.getUserByEmail((com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest) request,
              (io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.GetUserResponse>) responseObserver);
          break;
        case METHODID_CREATE_USER:
          serviceImpl.createUser((com.thomaskyaw.saas.identity.v1.CreateUserRequest) request,
              (io.grpc.stub.StreamObserver<com.thomaskyaw.saas.identity.v1.CreateUserResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getVerifyCredentialsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.thomaskyaw.saas.identity.v1.VerifyCredentialsRequest,
              com.thomaskyaw.saas.identity.v1.VerifyCredentialsResponse>(
                service, METHODID_VERIFY_CREDENTIALS)))
        .addMethod(
          getGetUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.thomaskyaw.saas.identity.v1.GetUserRequest,
              com.thomaskyaw.saas.identity.v1.GetUserResponse>(
                service, METHODID_GET_USER)))
        .addMethod(
          getGetUserByEmailMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest,
              com.thomaskyaw.saas.identity.v1.GetUserResponse>(
                service, METHODID_GET_USER_BY_EMAIL)))
        .addMethod(
          getCreateUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.thomaskyaw.saas.identity.v1.CreateUserRequest,
              com.thomaskyaw.saas.identity.v1.CreateUserResponse>(
                service, METHODID_CREATE_USER)))
        .build();
  }

  private static abstract class IdentityServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    IdentityServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.thomaskyaw.saas.identity.v1.IdentityProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("IdentityService");
    }
  }

  private static final class IdentityServiceFileDescriptorSupplier
      extends IdentityServiceBaseDescriptorSupplier {
    IdentityServiceFileDescriptorSupplier() {}
  }

  private static final class IdentityServiceMethodDescriptorSupplier
      extends IdentityServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    IdentityServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (IdentityServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new IdentityServiceFileDescriptorSupplier())
              .addMethod(getVerifyCredentialsMethod())
              .addMethod(getGetUserMethod())
              .addMethod(getGetUserByEmailMethod())
              .addMethod(getCreateUserMethod())
              .build();
        }
      }
    }
    return result;
  }
}
