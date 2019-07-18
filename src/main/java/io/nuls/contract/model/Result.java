package io.nuls.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nuls.contract.utils.JSONUtils;
import io.nuls.contract.utils.StringUtils;
import io.nuls.core.constant.ErrorCode;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private boolean success;

    private String msg;

    private ErrorCode errorCode;

    private T data;

    public Result(boolean success) {
        this.success = success;
    }

    public Result(boolean success, ErrorCode errorCode, T data) {
        this.success = success;
        this.errorCode = errorCode;
        this.data = data;
    }

    public Result(boolean success, ErrorCode errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    public Result(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    @JsonIgnore
    public boolean isFailed() {
        return !success;
    }

    public Result<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMsg() {
        if (StringUtils.isBlank(msg)) {
            return errorCode.getMsg();
        }
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("result:{");
        buffer.append("\"success\": ").append(success).append(",");
        buffer.append("\"msg\": \"").append(msg).append("\",");
        if (errorCode == null) {
            buffer.append("\"errorCode\": \"\",");
        } else {
            buffer.append("\"errorCode\": \"").append(errorCode.getCode()).append("\",");
        }
        if (data != null) {
            try {
                buffer.append("\"entity\":").append(JSONUtils.obj2json(data));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        buffer.append("}");
        return buffer.toString();
    }

    public static Result getSuccess(ErrorCode successCode) {
        return new Result(true, successCode);
    }

    public static Result getFailed(ErrorCode errorCode) {
        return new Result(false, errorCode, null);
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
