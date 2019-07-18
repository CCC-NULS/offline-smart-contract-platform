package io.nuls.contract.account.storage;

import io.nuls.base.data.Address;
import io.nuls.contract.account.model.po.AccountPo;

import java.util.List;

public interface AccountStorageService {
    /**
     * 创建账户
     * Create account
     * @param account
     * @return
     */
    boolean saveAccount(AccountPo account);

    /**
     * 根据账户获取账户信息
     * According to the account to obtain account information
     * @param address
     * @return the result of the opration
     */
    AccountPo getAccount(byte[] address);

    /**
     * 删除账户
     * Delete account
     * @param address Account address to be deleted
     * @return the result of the opration
     */
    boolean removeAccount(Address address);

    /**
     * 根据账户更新账户信息
     * Update account information according to the account.
     * @param account The account to be updated.
     * @return the result of the opration
     */
    boolean updateAccount(AccountPo account);


    /**
     * 获取所有账户
     * @return the result of the opration and Result<List<Account>>
     */
    List<AccountPo> getAccountList();

}
