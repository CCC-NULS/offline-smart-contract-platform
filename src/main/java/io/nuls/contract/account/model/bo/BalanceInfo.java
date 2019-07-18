package io.nuls.contract.account.model.bo;

import java.math.BigInteger;

public class BalanceInfo {

    private BigInteger totalBalance;

    private BigInteger balance;

    private BigInteger timeLock;

    private BigInteger consensusLock;

    private BigInteger freeze;

    private String nonce;

    private int nonceType;

    public BigInteger getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigInteger totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public BigInteger getTimeLock() {
        return timeLock;
    }

    public void setTimeLock(BigInteger timeLock) {
        this.timeLock = timeLock;
    }

    public BigInteger getConsensusLock() {
        return consensusLock;
    }

    public void setConsensusLock(BigInteger consensusLock) {
        this.consensusLock = consensusLock;
    }

    public BigInteger getFreeze() {
        return freeze;
    }

    public void setFreeze(BigInteger freeze) {
        this.freeze = freeze;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public int getNonceType() {
        return nonceType;
    }

    public void setNonceType(int nonceType) {
        this.nonceType = nonceType;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"totalBalance\":")
                .append('\"').append(totalBalance).append('\"');
        sb.append(",\"balance\":")
                .append(balance);
        sb.append(",\"timeLock\":")
                .append('\"').append(timeLock).append('\"');
        sb.append(",\"consensusLock\":")
                .append(consensusLock);
        sb.append(",\"freeze\":")
                .append(freeze);
        sb.append(",\"nonce\":")
                .append(nonce);
        sb.append(",\"nonceType\":")
                .append(nonceType);
        sb.append('}');
        return sb.toString();
    }
}
