package io.nuls.contract.service;

import io.nuls.base.signture.P2PHKSignature;
import io.nuls.contract.account.model.bo.Account;
import io.nuls.contract.account.model.bo.AccountKeyStore;
import io.nuls.contract.model.vo.AccountInfoVo;
import io.nuls.contract.account.model.bo.AccountInfo;
import io.nuls.contract.account.model.bo.BalanceInfo;
import io.nuls.core.exception.NulsException;

import java.util.List;
import java.util.Map;

public interface AccountService {

    public Account createAccount(int chainId,String password) throws NulsException;

    public boolean removeAccount(int chainId, String address, String password) throws NulsException;

    public Account getAccount(int chainId, String address) throws NulsException;

    public List<AccountInfoVo> getAccountList(int chainId) throws NulsException ;

    public BalanceInfo getAccountBalance(int chainId,int assetChainId, int assetId, String address) throws NulsException;

    public AccountInfo getAccountForChain(int chainId, String address) throws NulsException;

    /**
     * 数据摘要签名
     * sign digest data
     *
     * @param digest   data digest.
     * @param chainId
     * @param address  address of account.
     * @param password password of account.
     * @return the signData byte[].
     * @throws NulsException nulsException
     */
    P2PHKSignature signDigest(byte[] digest, int chainId, String address, String password) throws NulsException;

    boolean validationPassword(int chainId,String address, String password);

    /**
     * 从keyStore导入账户(密码用来验证keystore)
     * 1.从keyStore获取明文私钥(如果没有明文私钥,则通过密码从keyStore中的encryptedPrivateKey解出来)
     * 2.通过keyStore创建新账户,加密账户
     * 3.从数据库搜索此账户的别名,没有搜到则不设置(别名不从keyStore中获取,因为可能被更改)
     * 4.保存账户
     * import an account form account key store.
     *
     * @param keyStore  the keyStore of the account.
     * @param chainId
     * @param password  the password of account
     * @param overwrite
     * @return the result of the operation.
     * @throws NulsException
     */
    Account importAccountByKeyStore(AccountKeyStore keyStore, int chainId, String password, boolean overwrite) throws NulsException;

    String getPrivateKey(int chainId, String address, String password) throws NulsException;

    Account importAccountByPrikey(int chainId, String prikey, String password, boolean overwrite) throws NulsException;

    Map<Integer, String> getPrefixFromAccountModule() throws NulsException;
}
