package io.nuls.contract.service;

import io.nuls.contract.model.vo.TransactionInfo;
import io.nuls.core.exception.NulsException;

public interface TransactionService {

    public boolean broadcastTx(int chainId, String txHex) throws NulsException;

    public TransactionInfo getTx(int chainId, String txHash)throws NulsException;

}
