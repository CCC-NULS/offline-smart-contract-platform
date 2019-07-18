package io.nuls.contract.model;

import io.nuls.base.data.BlockHeader;
import io.nuls.base.data.Transaction;
import io.nuls.contract.model.txdata.ContractData;

public class ContractWrapperTransaction extends Transaction {

    private String txHex;

    private ContractResult contractResult;

    private ContractData contractData;

    /**
     * 接收到的交易顺序
     */
    private int order;

    private BlockHeader blockHeader;

    public ContractWrapperTransaction(Transaction tx, String txHex, ContractData contractData) {
        this.txHex = txHex;
        this.contractData = contractData;
        this.copyTx(tx);
    }

    public ContractWrapperTransaction(Transaction tx, ContractData contractData) {
        this.contractData = contractData;
        this.copyTx(tx);
    }

    private void copyTx(Transaction tx) {
        this.setType(tx.getType());
        this.setCoinData(tx.getCoinData());
        this.setTxData(tx.getTxData());
        this.setTime(tx.getTime());
        this.setTransactionSignature(tx.getTransactionSignature());
        this.setRemark(tx.getRemark());
    }

    public String getTxHex() {
        return txHex;
    }

    public void setTxHex(String txHex) {
        this.txHex = txHex;
    }

    public ContractResult getContractResult() {
        return contractResult;
    }

    public void setContractResult(ContractResult contractResult) {
        this.contractResult = contractResult;
    }

    public ContractData getContractData() {
        return contractData;
    }

    public void setContractData(ContractData contractData) {
        this.contractData = contractData;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public BlockHeader getBlockHeader() {
        return blockHeader;
    }

    public void setBlockHeader(BlockHeader blockHeader) {
        this.blockHeader = blockHeader;
    }
}
