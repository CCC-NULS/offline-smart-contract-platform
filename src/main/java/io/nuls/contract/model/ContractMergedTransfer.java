package io.nuls.contract.model;

import io.nuls.base.data.NulsHash;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ContractMergedTransfer {

    private byte[] from;
    private BigInteger value;
    private List<Output> outputs;


    /**
     * 智能合约交易hash
     */
    private NulsHash orginHash;

    /**
     * 合约转账(从合约转出)交易hash
     */
    private NulsHash hash;

    public ContractMergedTransfer() {
        outputs = new ArrayList<>();
    }

    public byte[] getFrom() {
        return from;
    }

    public void setFrom(byte[] from) {
        this.from = from;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }

    public NulsHash getOrginHash() {
        return orginHash;
    }

    public void setOrginHash(NulsHash orginHash) {
        this.orginHash = orginHash;
    }

    public NulsHash getHash() {
        return hash;
    }

    public void setHash(NulsHash hash) {
        this.hash = hash;
    }
}