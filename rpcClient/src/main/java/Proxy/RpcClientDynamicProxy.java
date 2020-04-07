package Proxy;

import netty.Client;
import protocol.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class RpcClientDynamicProxy<T> implements InvocationHandler {
    private Class<T> clazz;
    public RpcClientDynamicProxy(Class<T> clazz) throws Exception {
        this.clazz = clazz;
    }
    Client nettyClient;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (nettyClient==null) {
            nettyClient = new Client();
            nettyClient.connect();
        }
        Thread.sleep(1000);
        RpcRequest request = new RpcRequest();
        String requestId = UUID.randomUUID().toString();

        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        Class<?>[] parameterTypes = method.getParameterTypes();

        request.setRequestId(requestId);
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameterTypes(parameterTypes);
        request.setParameters(args);


        return nettyClient.send(request).getResult();

    }
}
