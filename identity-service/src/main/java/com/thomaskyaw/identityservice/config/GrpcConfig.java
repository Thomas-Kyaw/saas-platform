package com.thomaskyaw.identityservice.config;

import com.thomaskyaw.common.grpc.GrpcHeaderInterceptor;
import io.grpc.ServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public GrpcHeaderInterceptor grpcHeaderInterceptor() {
        return new GrpcHeaderInterceptor();
    }
}
