package com.thomaskyaw.authservice.grpc;

import com.thomaskyaw.saas.identity.v1.GetUserByEmailRequest;
import com.thomaskyaw.saas.identity.v1.GetUserResponse;
import com.thomaskyaw.saas.identity.v1.IdentityServiceGrpc;
import com.thomaskyaw.common.grpc.GrpcHeaderInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IdentityServiceGrpcClient {

    private IdentityServiceGrpc.IdentityServiceBlockingStub identityServiceStub;

    @Value("${identity.service.host:localhost}")
    private String identityHost;

    @Value("${identity.service.port:9090}")
    private int identityPort;

    @PostConstruct
    public void init() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(identityHost, identityPort)
                .usePlaintext()
                .intercept(new GrpcHeaderInterceptor())
                .build();
        this.identityServiceStub = IdentityServiceGrpc.newBlockingStub(channel);
    }

    public GetUserResponse getUserByEmail(String email) {
        return identityServiceStub.getUserByEmail(
                GetUserByEmailRequest.newBuilder().setEmail(email).build()
        );
    }
}
