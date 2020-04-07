package protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应
 */
@Data
public class RpcResponse  implements Serializable {
    // 调用编号
    private String requestId;
    // 抛出的异常
    private Throwable throwable;
    // 返回结果
    private Object result;

}
