package io.nuls.contract.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.nuls.core.basic.Result;
import io.nuls.core.constant.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RpcResult<T> {

    private boolean success;

    private T result;

    private RpcResultError error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public RpcResult(){

    };
    public RpcResult(boolean isSucess){
        this.success=isSucess;
    }

    public RpcResult setResult(T result) {
        this.result = result;
        return this;
    }

    public RpcResultError getError() {
        return error;
    }

    public RpcResult setError(RpcResultError error) {
        this.error = error;
        return this;
    }

    public static <T> RpcResult success(T t) {
        RpcResult rpcResult = new RpcResult(true);
        rpcResult.setResult(t);
        return rpcResult;
    }

    public static RpcResult failed(ErrorCode errorCode) {
        RpcResult rpcResult = new RpcResult(false);
        RpcResultError error = new RpcResultError(errorCode);
        rpcResult.setError(error);
        return rpcResult;
    }
    public static RpcResult failed(ErrorCode errorCode,Object data) {
        RpcResult rpcResult = new RpcResult(false);
        RpcResultError error = new RpcResultError(errorCode,data);
        rpcResult.setError(error);
        return rpcResult;
    }

 /*   public static RpcResult failed(Result result) {
        RpcResult rpcResult = new RpcResult(false);
        RpcResultError error = new RpcResultError(result.getErrorCode().getCode(), result.getMsg(), null);
        rpcResult.setError(error);
        return rpcResult;
    }*/

    public static RpcResult paramError() {
        RpcResult rpcResult = new RpcResult(false);
        RpcResultError error = new RpcResultError(RpcErrorCode.PARAMETER_ERROR.getCode(), RpcErrorCode.PARAMETER_ERROR.getMsg(), null);
        rpcResult.setError(error);
        return rpcResult;
    }

    public static RpcResult paramError(String code,String message) {
        RpcResult rpcResult = new RpcResult(false);
        RpcResultError error = new RpcResultError(code, message, null);
        rpcResult.setError(error);
        return rpcResult;
    }

    public static RpcResult paramError(String data) {
        RpcResult rpcResult = new RpcResult(false);
        RpcResultError error = new RpcResultError(RpcErrorCode.PARAMETER_ERROR.getCode(), RpcErrorCode.PARAMETER_ERROR.getMsg(), data);
        rpcResult.setError(error);
        return rpcResult;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"success\":")
                .append('\"').append(success).append('\"');
        sb.append(",\"result\":")
                .append('\"').append(result).append('\"');
        sb.append(",\"error\":")
                .append(error);
        sb.append('}');
        return sb.toString();
    }
}
