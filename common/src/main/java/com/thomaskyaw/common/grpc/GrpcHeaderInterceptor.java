package com.thomaskyaw.common.grpc;

import com.thomaskyaw.common.context.UserContext;
import io.grpc.*;

public class GrpcHeaderInterceptor implements ServerInterceptor, ClientInterceptor {

    public static final Metadata.Key<String> USER_ID_KEY = Metadata.Key.of("x-user-id", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> TENANT_ID_KEY = Metadata.Key.of("x-tenant-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        final String userId = headers.get(USER_ID_KEY);
        final String tenantId = headers.get(TENANT_ID_KEY);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(next.startCall(call, headers)) {
            @Override
            public void onMessage(ReqT message) {
                try {
                    setContext(userId, tenantId);
                    super.onMessage(message);
                } finally {
                    UserContext.clear();
                }
            }

            @Override
            public void onHalfClose() {
                try {
                    setContext(userId, tenantId);
                    super.onHalfClose();
                } finally {
                    UserContext.clear();
                }
            }

            @Override
            public void onCancel() {
                try {
                    setContext(userId, tenantId);
                    super.onCancel();
                } finally {
                    UserContext.clear();
                }
            }

            @Override
            public void onReady() {
                try {
                    setContext(userId, tenantId);
                    super.onReady();
                } finally {
                    UserContext.clear();
                }
            }

            private void setContext(String userId, String tenantId) {
                if (userId != null) UserContext.setUserId(userId);
                if (tenantId != null) UserContext.setTenantId(tenantId);
            }
        };
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String userId = UserContext.getUserId();
                String tenantId = UserContext.getTenantId();

                if (userId != null) {
                    headers.put(USER_ID_KEY, userId);
                }
                if (tenantId != null) {
                    headers.put(TENANT_ID_KEY, tenantId);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
