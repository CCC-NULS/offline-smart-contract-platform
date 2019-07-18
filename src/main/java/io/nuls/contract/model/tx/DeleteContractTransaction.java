package io.nuls.contract.model.tx;

import io.nuls.contract.model.txdata.DeleteContractData;

import static io.nuls.core.constant.TxType.DELETE_CONTRACT;

public class DeleteContractTransaction extends ContractBaseTransaction<DeleteContractData> {

    public DeleteContractTransaction() {
        super(DELETE_CONTRACT);
    }

    @Override
    protected DeleteContractData newInstance() {
        return new DeleteContractData();
    }
}
