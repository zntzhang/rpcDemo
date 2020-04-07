package protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求
 */
@Data
public class RpcRequest implements Serializable {
    // 调用编号
    private String requestId;
    // 类名
    private String className;
    // 方法名
    private String methodName;
    // 请求参数的数据类型
    private Class<?>[] parameterTypes;
    // 请求的参数
    private Object[] parameters;

}
