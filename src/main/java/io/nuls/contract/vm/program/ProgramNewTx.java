package io.nuls.contract.vm.program;

import io.nuls.base.data.Transaction;

public class ProgramNewTx {
    private String txHash;
    private String txString;
    private transient Transaction tx;

    public ProgramNewTx() {
    }

    public ProgramNewTx(String txHash, String txString, Transaction tx) {
        this.txHash = txHash;
        this.txString = txString;
        this.tx = tx;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getTxString() {
        return txString;
    }

    public void setTxString(String txString) {
        this.txString = txString;
    }

    public Transaction getTx() {
        return tx;
    }

    public void setTx(Transaction tx) {
        this.tx = tx;
    }
}
