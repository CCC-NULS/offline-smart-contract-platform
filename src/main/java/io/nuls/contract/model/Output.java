package io.nuls.contract.model;

import java.math.BigInteger;

public class Output {
    private byte[] to;
    private BigInteger value;

    public byte[] getTo() {
        return to;
    }

    public void setTo(byte[] to) {
        this.to = to;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }
}
