package io.nuls.contract.service.impl;

import io.nuls.base.basic.AddressTool;
import io.nuls.base.data.Address;
import io.nuls.base.signture.P2PHKSignature;
import io.nuls.base.signture.SignatureUtil;
import io.nuls.contract.account.model.bo.Account;
import io.nuls.contract.account.model.bo.AccountKeyStore;
import io.nuls.contract.model.vo.AccountInfoVo;
import io.nuls.contract.account.model.po.AccountPo;
import io.nuls.contract.account.storage.AccountStorageService;
import io.nuls.contract.account.utils.AccountTool;
import io.nuls.contract.account.model.bo.AccountInfo;
import io.nuls.contract.account.model.bo.BalanceInfo;
import io.nuls.contract.model.RpcErrorCode;
import io.nuls.contract.service.AccountKeyStoreService;
import io.nuls.contract.service.AccountService;
import io.nuls.core.crypto.AESEncrypt;
import io.nuls.core.crypto.ECKey;
import io.nuls.core.crypto.HexUtil;
import io.nuls.core.exception.CryptoException;
import io.nuls.core.exception.NulsException;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.log.Log;
import io.nuls.core.model.FormatValidUtils;
import io.nuls.core.model.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AccountServiceImpl implements AccountService {

    private Lock locker = new ReentrantLock();

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private AccountStorageService accountStorageService;

    @Autowired
    private AccountKeyStoreService accountKeyStoreService;

    @Override
    public Account createAccount(int chainId, String password) throws NulsException {
        locker.lock();
        try {
            Account account = AccountTool.createAccount(chainId);
            if (StringUtils.isNotBlank(password)) {
                account.encrypt(password);
            }
            AccountPo po = new AccountPo(account);
            boolean result = accountStorageService.saveAccount(po);
            if(result){
                accountKeyStoreService.backupAccountToKeyStore(null, chainId, account.getAddress().getBase58(), password);
            }
            return account;
        } catch (NulsException e) {
            throw new NulsException(e.getErrorCode());
        }finally {
            locker.unlock();
        }
    }

    @Override
    public boolean removeAccount(int chainId, String address, String password) throws NulsException {
        //check params
        if (!AddressTool.validAddress(chainId, address)) {
            throw new NulsRuntimeException(RpcErrorCode.ADDRESS_ERROR);
        }
        //Check whether the account exists
        byte[] addressBytes = AddressTool.getAddress(address);
        AccountPo accountPo=accountStorageService.getAccount(addressBytes);
        if (accountPo == null) {
            throw new NulsRuntimeException(RpcErrorCode.ACCOUNT_NOT_EXIST);
        }
        Account account=accountPo.toAccount();
        //The account is encrypted, verify password
        if (account.isEncrypted()&&!account.validatePassword(password)) {
            throw new NulsRuntimeException(RpcErrorCode.PASSWORD_IS_WRONG);
        }
        boolean result;
        try {
            //Delete the account from the database
            result = accountStorageService.removeAccount(account.getAddress());
        } catch (Exception e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(RpcErrorCode.DB_DELETE_ERROR);
        }
        return result;
    }


    @Override
    public Account getAccount(int chainId, String address) throws NulsException {
        if (!AddressTool.validAddress(chainId, address)) {
            throw new NulsException(RpcErrorCode.ADDRESS_ERROR);
        }
        byte[] addressBytes = AddressTool.getAddress(address);
        AccountPo accountPo=accountStorageService.getAccount(addressBytes);
        Account account=null;
        if(accountPo!=null){
            account=accountPo.toAccount();
        }
        return account;
    }

    @Override
    public List<AccountInfoVo> getAccountList(int chainId) throws NulsException  {
        List<AccountInfoVo> accountList = new ArrayList<>();
        List<AccountPo> accountPoList=accountStorageService.getAccountList();
        if (null == accountPoList || accountPoList.isEmpty()) {
            return accountList;
        }
        for (AccountPo po : accountPoList) {
            if(chainId==po.getChainId()){
                AccountInfoVo accountInfoVo =po.toAccountInfoVo();
                AccountInfo accountInfo =this.getAccountForChain(chainId,po.getAddress());
                accountInfoVo.setAlias(accountInfo.getAlias());
                accountInfoVo.setBalance(accountInfo.getBalance());
                accountInfoVo.setTotalBalance(accountInfo.getTotalBalance());
                accountList.add(accountInfoVo);
            }
        }
        Collections.sort(accountList, (AccountInfoVo o1, AccountInfoVo o2) -> (o2.getCreateTime().compareTo(o1.getCreateTime())));
        return accountList;
    }


    @Override
    public BalanceInfo getAccountBalance(int chainId, int assetChainId,int assetId, String address) throws NulsException {
        BalanceInfo balanceInfo=null;
        try {
            balanceInfo= httpClient.getRpcHttpClient().invoke("getAccountBalance",new Object[]{chainId,assetChainId,assetId,address},BalanceInfo.class);
        } catch (Throwable e) {
            Log.error(e.getMessage());
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR,e.getMessage());
        }
        return balanceInfo;
    }

    @Override
    public AccountInfo getAccountForChain(int chainId, String address) throws NulsException {
        AccountInfo accountInfo=null;
        try {
            accountInfo= httpClient.getRpcHttpClient().invoke("getAccount",new Object[]{chainId,address}, AccountInfo.class);
        }catch (Throwable e) {
            Log.error(e.getMessage());
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR,e.getMessage());
        }
        return accountInfo;
    }

    @Override
    public P2PHKSignature signDigest(byte[] digest, int chainId, String address, String password) throws NulsException {
        if (null == digest || digest.length == 0) {
            throw new NulsException(RpcErrorCode.PARAMETER_ERROR);
        }
        byte[] addressBytes = AddressTool.getAddress(address);
        AccountPo accountPo=accountStorageService.getAccount(addressBytes);
        try {
            //根据密码获得ECKey get ECKey from Password
            ECKey  ecKey = accountPo.getEcKey(password);
            byte[] signBytes = SignatureUtil.signDigest(digest, ecKey).serialize();
            return new P2PHKSignature(signBytes, ecKey.getPubKey());
        }catch (IOException e) {
            Log.error(e.getMessage());
            throw new NulsException(RpcErrorCode.IO_ERROR,e);
        }
    }

    @Override
    public boolean validationPassword(int chainId, String address, String password) {
        byte[] addressBytes = AddressTool.getAddress(address);
        AccountPo accountPo=accountStorageService.getAccount(addressBytes);
        boolean result=true;
        if(accountPo!=null&&accountPo.isEncrypted()){
            result =accountPo.validatePassword(password);
        }
        return result;
    }

    @Override
    public Account importAccountByKeyStore(AccountKeyStore keyStore, int chainId, String password, boolean overwrite) throws NulsException {
        if (null == keyStore || StringUtils.isBlank(keyStore.getAddress())) {
            throw new NulsException(RpcErrorCode.PARAMETER_ERROR);
        }
        if (!AddressTool.validAddress(chainId, keyStore.getAddress())) {
            throw new NulsException(RpcErrorCode.ADDRESS_ERROR);
        }
        if (StringUtils.isNotBlank(password)&&!FormatValidUtils.validPassword(password)) {
            throw new NulsException(RpcErrorCode.PASSWORD_IS_WRONG);
        }
        if (!overwrite) {
            byte[] addressBytes = AddressTool.getAddress(keyStore.getAddress());
            //Query account already exists
            AccountPo accountPo = accountStorageService.getAccount(addressBytes);
            if (null != accountPo) {
                throw new NulsException(RpcErrorCode.ACCOUNT_EXIST);
            }
        }

        Account account;
        byte[] priKey;
        //if the private key is not encrypted, it is not empty
        if (null != keyStore.getPrikey() && keyStore.getPrikey().length > 0) {
            if (!ECKey.isValidPrivteHex(HexUtil.encode(keyStore.getPrikey()))) {
                throw new NulsException(RpcErrorCode.PARAMETER_ERROR);
            }
            //create account by private key
            priKey = keyStore.getPrikey();
            account = AccountTool.createAccount(chainId, HexUtil.encode(priKey));
            //如果私钥生成的地址和keystore的地址不相符，说明私钥错误
            //if the address generated by the private key does not match the address of the keystore, the private key error
            if (!account.getAddress().getBase58().equals(keyStore.getAddress())) {
                throw new NulsException(RpcErrorCode.PRIVATE_KEY_WRONG);
            }
        } else if (null == keyStore.getPrikey() && null != keyStore.getEncryptedPrivateKey()) {
            try {
                //create account by private key
                priKey = AESEncrypt.decrypt(HexUtil.decode(keyStore.getEncryptedPrivateKey()), password);
                account = AccountTool.createAccount(chainId, HexUtil.encode(priKey));
            } catch (CryptoException e) {
                throw new NulsException(RpcErrorCode.PASSWORD_IS_WRONG);
            }
            //如果私钥生成的地址和keystore的地址不相符，说明私钥错误
            //if the address generated by the private key does not match the address of the keystore, the private key error
            if (!account.getAddress().getBase58().equals(keyStore.getAddress())) {
                throw new NulsException(RpcErrorCode.PRIVATE_KEY_WRONG);
            }
        } else {
            throw new NulsException(RpcErrorCode.PARAMETER_ERROR);
        }

        if(StringUtils.isNotBlank(password)){
            account.encrypt(password);
        }

        accountStorageService.saveAccount(new AccountPo(account));

        //backup account to keystore
        accountKeyStoreService.backupAccountToKeyStore(null, chainId, account.getAddress().getBase58(), password);

        return account;
    }

    @Override
    public Account importAccountByPrikey(int chainId, String prikey, String password, boolean overwrite) throws NulsException {
        //check params
        if (!ECKey.isValidPrivteHex(prikey)) {
            throw new NulsException(RpcErrorCode.PRIVATE_KEY_WRONG);
        }
        if (StringUtils.isNotBlank(password)&&!FormatValidUtils.validPassword(password)) {
            throw new NulsException(RpcErrorCode.PASSWORD_IS_WRONG);
        }
        //not allowed to cover
        if (!overwrite) {
            Address address = AccountTool.newAddress(chainId, prikey);
            //Query account already exists
            Account account = this.getAccount(chainId, address.getBase58());
            if (null != account) {
                throw new NulsException(RpcErrorCode.ACCOUNT_EXIST);
            }
        }
        //create account by private key
        Account account = AccountTool.createAccount(chainId, prikey);

        //encrypting account private key
        if(StringUtils.isNotBlank(password)){
            account.encrypt(password);
        }

        //save account to storage
        accountStorageService.saveAccount(new AccountPo(account));

        //backup account to keystore
        accountKeyStoreService.backupAccountToKeyStore(null, chainId, account.getAddress().getBase58(), password);

        return account;
    }

    @Override
    public String getPrivateKey(int chainId, String address, String password) throws NulsException {
        //check whether the account exists
        Account account = this.getAccount(chainId, address);
        if (null == account) {
            throw new NulsException(RpcErrorCode.ACCOUNT_NOT_EXIST);
        }
        //加过密(有密码) 就验证密码 Already encrypted(Added password), verify password
        if (account.isEncrypted()) {
            byte[] priKeyBytes = account.getPriKey(password);
            return HexUtil.encode(priKeyBytes);
        } else {
            return  HexUtil.encode(account.getPriKey());
        }
    }

    @Override
    public Map<Integer, String> getPrefixFromAccountModule() throws NulsException{
        Map<Integer, String> address_prefix_map = new HashMap<Integer, String>();
        try {
            List<Map<String, Object>> addressPrefixList= httpClient.getRpcHttpClient().invoke("getAllAddressPrefix",new Object[]{},List.class);
            if (addressPrefixList!=null && addressPrefixList.size() > 0) {
                for (Map<String, Object> addressPrefixMap : addressPrefixList) {
                    address_prefix_map.put(Integer.valueOf(addressPrefixMap.get("chainId").toString()), String.valueOf(addressPrefixMap.get("addressPrefix")));
                }
            }
        } catch (Throwable e) {
            Log.error(e.getMessage());
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR,e.getMessage());
        }
        return address_prefix_map;
    }

}
