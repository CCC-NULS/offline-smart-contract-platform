package io.nuls.contract.model.tx;

import io.nuls.contract.model.txdata.CreateContractData;

import static io.nuls.core.constant.TxType.CREATE_CONTRACT;

public class CreateContractTransaction extends ContractBaseTransaction<CreateContractData> {

    public CreateContractTransaction() {
        super(CREATE_CONTRACT);
    }

    @Override
    protected CreateContractData newInstance() {
        return new CreateContractData();
    }
}
