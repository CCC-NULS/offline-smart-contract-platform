package io.nuls.contract.model.deserialization;

import io.nuls.base.basic.AddressTool;
import io.nuls.contract.model.txdata.ContractData;

import static io.nuls.contract.utils.ContractUtil.bigInteger2String;

public class CallContractDataDto {
    private String sender;
    private String contractAddress;
    private String value;
    private long gasLimit;
    private long price;
    private String methodName;
    private String methodDesc;
    private String[][] args;

    public CallContractDataDto(ContractData call) {
        this.sender = AddressTool.getStringAddressByBytes(call.getSender());
        this.contractAddress = AddressTool.getStringAddressByBytes(call.getContractAddress());
        this.value = bigInteger2String(call.getValue());
        this.gasLimit = call.getGasLimit();
        this.price = call.getPrice();
        this.methodName = call.getMethodName();
        this.methodDesc = call.getMethodDesc();
        this.args = call.getArgs();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public String[][] getArgs() {
        return args;
    }

    public void setArgs(String[][] args) {
        this.args = args;
    }
}
