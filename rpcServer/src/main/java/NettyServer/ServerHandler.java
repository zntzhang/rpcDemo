package NettyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.RpcRequest;
import protocol.RpcResponse;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    // 简单处理了一下，后续可以放在bean中
    Map<String, String> map = new HashMap<String, String>() {{
        put("remote.IUserRemote", "service.UserRemoteImpl");
    }};


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest) msg;
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try {
            // 收到请求后开始处理请求
            Object handler = handler(rpcRequest);
            rpcResponse.setResult(handler);
        } catch (Throwable throwable) {
            // 如果抛出异常也将异常存入 response 中
            rpcResponse.setThrowable(throwable);
            throwable.printStackTrace();
        }
        // 操作完以后写入 netty 的上下文中。netty 自己处理返回值。
        ctx.writeAndFlush(rpcResponse);
    }

    private Object handler(RpcRequest request) throws Throwable {


        String service = request.getClassName();
        service = map.get(service);
        Class serviceClass = Class.forName(service);


        Object obj = serviceClass.newInstance();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        Method method = serviceClass.getDeclaredMethod(methodName, parameterTypes);
        return method.invoke(obj, parameters);
    }


}