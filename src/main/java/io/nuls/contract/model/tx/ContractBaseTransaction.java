package io.nuls.contract.model.tx;

import io.nuls.base.data.BaseNulsData;
import io.nuls.base.data.CoinData;
import io.nuls.base.data.Transaction;
import io.nuls.contract.model.ContractResult;
import io.nuls.core.exception.NulsException;

import java.io.IOException;

public abstract class ContractBaseTransaction <T extends BaseNulsData> extends Transaction {

    private ContractResult contractResult;

    private CoinData coinDataObj;

    private T txDataObj;

    protected ContractBaseTransaction() {
    }

    protected ContractBaseTransaction(int txType) {
        super(txType);

    }

    public CoinData getCoinDataObj() throws NulsException {
        if (coinDataObj == null) {
            CoinData coinData = new CoinData();
            coinData.parse(this.getCoinData(), 0);
            coinDataObj = coinData;
        }
        return coinDataObj;
    }

    public T getTxDataObj() throws NulsException {
        if (txDataObj == null) {
            txDataObj = newInstance();
            if (txDataObj != null) {
                txDataObj.parse(this.getTxData(), 0);
            }
        }
        return txDataObj;
    }

    protected abstract T newInstance();

    public void serializeData() throws IOException {
        if (this.getCoinData() == null && coinDataObj != null) {
            this.setCoinData(coinDataObj.serialize());
        }
        if (this.getTxData() == null && txDataObj != null) {
            this.setTxData(txDataObj.serialize());
        }
    }

    public void copyTx(Transaction tx) {
        this.setType(tx.getType());
        this.setCoinData(tx.getCoinData());
        this.setTxData(tx.getTxData());
        this.setTime(tx.getTime());
        this.setTransactionSignature(tx.getTransactionSignature());
        this.setRemark(tx.getRemark());
    }

    public ContractResult getContractResult() {
        return contractResult;
    }

    public void setContractResult(ContractResult contractResult) {
        this.contractResult = contractResult;
    }

    public void setCoinDataObj(CoinData coinDataObj) {
        this.coinDataObj = coinDataObj;
    }

    public void setTxDataObj(T txDataObj) {
        this.txDataObj = txDataObj;
    }
}
