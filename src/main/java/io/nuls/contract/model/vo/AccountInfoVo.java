package io.nuls.contract.model.vo;

import java.io.Serializable;
import java.math.BigInteger;

public class AccountInfoVo implements Serializable {

    private int chainId;

    private String address;

    private String alias;

    private BigInteger balance;

    private BigInteger totalBalance;

    private Long createTime;

    public AccountInfoVo() {
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public BigInteger getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigInteger totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"address\":")
                .append('\"').append(address).append('\"');
        sb.append(",\"chainId\":")
                .append(chainId);
        sb.append(",\"balance\":")
                .append('\"').append(balance).append('\"');
        sb.append(",\"totalBalance\":")
                .append(totalBalance);
        sb.append('}');
        return sb.toString();
    }
}
