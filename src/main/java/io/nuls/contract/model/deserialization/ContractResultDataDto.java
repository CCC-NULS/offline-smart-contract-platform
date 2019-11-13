package io.nuls.contract.model.deserialization;

import io.nuls.contract.model.vo.NulsTransfer;
import io.nuls.contract.model.vo.TokenTransfer;

import java.util.List;

public class ContractResultDataDto {
    private String txHash;

    private String contractAddress;

    private boolean success;

    private String errorMessage;

    private String result;

    private long gasLimit;

    private long gasUsed;

    private long price;

    private String totalFee;

    private String txSizeFee;

    private String actualContractFee;

    private String refundFee;

    private String value;

    //private String balance;

    private List<NulsTransfer> nulsTransfers;

    private List<TokenTransfer> tokenTransfers;

    private String remark;

    private List<String> contractTxList;

    private List<String> events;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public long getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(long gasUsed) {
        this.gasUsed = gasUsed;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getTxSizeFee() {
        return txSizeFee;
    }

    public void setTxSizeFee(String txSizeFee) {
        this.txSizeFee = txSizeFee;
    }

    public String getActualContractFee() {
        return actualContractFee;
    }

    public void setActualContractFee(String actualContractFee) {
        this.actualContractFee = actualContractFee;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<NulsTransfer> getNulsTransfers() {
        return nulsTransfers;
    }

    public void setNulsTransfers(List<NulsTransfer> nulsTransfers) {
        this.nulsTransfers = nulsTransfers;
    }

    public List<TokenTransfer> getTokenTransfers() {
        return tokenTransfers;
    }

    public void setTokenTransfers(List<TokenTransfer> tokenTransfers) {
        this.tokenTransfers = tokenTransfers;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getContractTxList() {
        return contractTxList;
    }

    public void setContractTxList(List<String> contractTxList) {
        this.contractTxList = contractTxList;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }
}
