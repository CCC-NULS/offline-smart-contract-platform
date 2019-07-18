package io.nuls.contract.account.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AccountInfo implements Serializable {

    private String address;

    private String alias;

    private int type;

    private int txCount;

    private BigInteger totalOut;

    private BigInteger totalIn;

    private BigInteger consensusLock;

    private BigInteger timeLock;

    private BigInteger balance;

    private BigInteger totalBalance;

    private BigInteger totalReward;

    private List<String> tokens;

    private String symbol;

    //是否是根据最新区块的交易新创建的账户，只为业务使用，不存储该字段
    @JsonIgnore
    private boolean isNew;

    public AccountInfo() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTxCount() {
        return txCount;
    }

    public void setTxCount(int txCount) {
        this.txCount = txCount;
    }

    public BigInteger getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(BigInteger totalOut) {
        this.totalOut = totalOut;
    }

    public BigInteger getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(BigInteger totalIn) {
        this.totalIn = totalIn;
    }

    public BigInteger getConsensusLock() {
        return consensusLock;
    }

    public void setConsensusLock(BigInteger consensusLock) {
        this.consensusLock = consensusLock;
    }

    public BigInteger getTimeLock() {
        return timeLock;
    }

    public void setTimeLock(BigInteger timeLock) {
        this.timeLock = timeLock;
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

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    @JsonIgnore
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public BigInteger getTotalReward() {
        return totalReward;
    }

    public void setTotalReward(BigInteger totalReward) {
        this.totalReward = totalReward;
    }

    public AccountInfo copy() {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.address = this.address;
        accountInfo.alias = this.alias;
        accountInfo.type = this.type;
        accountInfo.txCount = this.txCount;
        accountInfo.totalOut = new BigInteger(this.totalOut.toString());
        accountInfo.totalIn = new BigInteger(this.totalIn.toString());
        accountInfo.consensusLock = new BigInteger(this.consensusLock.toString());
        accountInfo.timeLock = new BigInteger(this.timeLock.toString());
        accountInfo.balance = new BigInteger(this.balance.toString());
        accountInfo.totalBalance = new BigInteger(this.totalBalance.toString());
        accountInfo.totalReward = new BigInteger(this.totalReward.toString());
        accountInfo.tokens = new ArrayList<>(this.tokens);
        return accountInfo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"address\":")
                .append('\"').append(address).append('\"');
        sb.append(",\"alias\":")
                .append(alias);
        sb.append(",\"type\":")
                .append('\"').append(type).append('\"');
        sb.append(",\"txCount\":")
                .append(txCount);
        sb.append('}');
        return sb.toString();
    }
}
