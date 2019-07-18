package io.nuls.contract.model.tx;

import io.nuls.contract.model.txdata.ContractTransferData;

import static io.nuls.contract.constant.TxType.CONTRACT_TRANSFER;

public class ContractTransferTransaction extends ContractBaseTransaction<ContractTransferData> {

public ContractTransferTransaction() {
        this.setType(CONTRACT_TRANSFER);
        }

@Override
protected ContractTransferData newInstance() {
        return new ContractTransferData();
        }
}
