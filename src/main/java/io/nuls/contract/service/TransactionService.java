package io.nuls.contract.service;

import io.nuls.core.exception.NulsException;

public interface TransactionService {

    public boolean broadcastTx(int chainId, String txHex) throws NulsException;

}
