package io.nuls.contract.model.vo;

import io.nuls.base.basic.AddressTool;
import io.nuls.core.constant.TxType;
import org.springframework.cache.CacheManager;

import java.math.BigInteger;
import java.util.List;

public class TransactionInfo {

    private String hash;

    private int type;

    private long height;

    private int size;

    private FeeInfo fee;

    private long createTime;

    private String remark;

    private String txDataHex;

    private TxDataInfo txData;

    private List<TxDataInfo> txDataList;

    private List<CoinFromInfo> coinFroms;

    private List<CoinToInfo> coinTos;

    private BigInteger value;

    private int status;

    public void calcValue() {
        BigInteger value = BigInteger.ZERO;
        if (coinTos != null) {
            for (CoinToInfo output : coinTos) {
                value = value.add(output.getAmount());
            }
        }
        this.value = value;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public FeeInfo getFee() {
        return fee;
    }

    public void setFee(FeeInfo fee) {
        this.fee = fee;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTxDataHex() {
        return txDataHex;
    }

    public void setTxDataHex(String txDataHex) {
        this.txDataHex = txDataHex;
    }

    public TxDataInfo getTxData() {
        return txData;
    }

    public void setTxData(TxDataInfo txData) {
        this.txData = txData;
    }

    public List<TxDataInfo> getTxDataList() {
        return txDataList;
    }

    public void setTxDataList(List<TxDataInfo> txDataList) {
        this.txDataList = txDataList;
    }

    public List<CoinFromInfo> getCoinFroms() {
        return coinFroms;
    }

    public void setCoinFroms(List<CoinFromInfo> coinFroms) {
        this.coinFroms = coinFroms;
    }

    public List<CoinToInfo> getCoinTos() {
        return coinTos;
    }

    public void setCoinTos(List<CoinToInfo> coinTos) {
        this.coinTos = coinTos;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
