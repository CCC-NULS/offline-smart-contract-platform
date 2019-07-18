package io.nuls.contract.model.tx;

import io.nuls.contract.model.txdata.CallContractData;

import static io.nuls.core.constant.TxType.CALL_CONTRACT;

public class CallContractTransaction extends ContractBaseTransaction<CallContractData> {


    public CallContractTransaction() {
        super(CALL_CONTRACT);
    }

    @Override
    protected CallContractData newInstance() {
        return new CallContractData();
    }
}
