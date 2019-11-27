package io.nuls.contract.service;

import io.nuls.contract.account.model.bo.AccountKeyStore;
import io.nuls.core.exception.NulsException;

public interface AccountKeyStoreService {
    /**
     * 备份账户到keyStore
     * backup account to keyStore
     *
     * @param path
     * @param chainId
     * @param address  the address of the account.
     * @param password the password of the account key store.
     * @return KeyStore path
     */
    String backupAccountToKeyStore(String path, int chainId, String address, String password) throws NulsException;

    public AccountKeyStore accountToKeyStore(int chainId, String address, String password) throws NulsException;
}
