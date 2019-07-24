package io.nuls.contract.service.impl;

import io.nuls.base.basic.AddressTool;
import io.nuls.contract.account.model.bo.Account;
import io.nuls.contract.account.model.bo.AccountKeyStore;
import io.nuls.contract.account.model.po.AccountKeyStoreDto;
import io.nuls.contract.autoconfig.ApiModuleInitTool;
import io.nuls.contract.autoconfig.ApiModuleInfoConfig;
import io.nuls.contract.constant.AccountConstant;
import io.nuls.contract.model.RpcErrorCode;
import io.nuls.contract.service.AccountKeyStoreService;
import io.nuls.contract.service.AccountService;
import io.nuls.core.crypto.HexUtil;
import io.nuls.core.exception.NulsException;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.log.Log;
import io.nuls.core.model.StringUtils;
import io.nuls.core.parse.JSONUtils;
import io.nuls.core.rockdb.util.DBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

@Service
public class AccountKeyStoreServiceImpl implements AccountKeyStoreService {

    @Autowired
    private ApiModuleInfoConfig infoConfig;

    @Autowired
    private AccountService accountService;

    @Override
    public String backupAccountToKeyStore(String path, int chainId, String address, String password) throws NulsException {
        if(path==null){
            path=infoConfig.getKeystorePath();
        }
        //export account to keystore
        AccountKeyStore accountKeyStore = this.accountToKeyStore(chainId, address, password);
        //backup keystore files
        String backupPath = this.backUpKeyStore(path, new AccountKeyStoreDto(accountKeyStore));
        return backupPath;


    }

    /**
     * 账户转为keystore
     *
     * @param chainId
     * @param address
     * @param password
     * @return
     */
    public AccountKeyStore accountToKeyStore(int chainId, String address, String password) throws NulsException {
        //check params
        if (!AddressTool.validAddress(chainId, address)) {
            throw new NulsRuntimeException(RpcErrorCode.ADDRESS_ERROR);
        }
        //check the account is exist
        Account account = accountService.getAccount(chainId, address);
        if (null == account) {
            throw new NulsRuntimeException(RpcErrorCode.ACCOUNT_NOT_EXIST);
        }
        AccountKeyStore accountKeyStore = new AccountKeyStore();
        //验证密码
        //verify the password
        if (account.isEncrypted()&&!account.validatePassword(password)) {
            throw new NulsException(RpcErrorCode.PASSWORD_IS_WRONG);
        }
        //如果账户加密,不导出明文私钥
        //if the account is encrypted , the plaintext private key is not exported
        if (account.isEncrypted()) {
            accountKeyStore.setEncryptedPrivateKey(HexUtil.encode(account.getEncryptedPriKey()));
        } else {
            accountKeyStore.setPrikey(account.getPriKey());
        }
        accountKeyStore.setAddress(account.getAddress().toString());
        accountKeyStore.setPubKey(account.getPubKey());
        return accountKeyStore;
    }

    /**
     * 备份keystore文件
     * backup keystore file
     */
    public String backUpKeyStore(String path, AccountKeyStoreDto accountKeyStoreDto) throws NulsException {
        //如果备份地址为空，则使用系统默认备份地址
        //if the backup address is empty, the default backup address of the system is used
        if (StringUtils.isBlank(path)) {
            if (StringUtils.isBlank(ApiModuleInitTool.ACCOUNTKEYSTORE_FOLDER_NAME)) {
                URL resource = ClassLoader.getSystemClassLoader().getResource("");
                path = resource.getPath();
            } else {
                path = ApiModuleInitTool.ACCOUNTKEYSTORE_FOLDER_NAME;
            }
            try {
                path = URLDecoder.decode(path, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.error(e);
                throw new NulsException(RpcErrorCode.DATA_PARSE_ERROR,e);
            }
        }
        File backupFile = DBUtils.loadDataPath(path);
        //if not directory,create directory
        if (!backupFile.isDirectory()) {
            if (!backupFile.mkdirs()) {
                throw new NulsException(RpcErrorCode.FILE_OPERATION_FAILD);
            }
            if (!backupFile.exists() && !backupFile.mkdir()) {
                throw new NulsException(RpcErrorCode.FILE_OPERATION_FAILD);
            }
        }
        //根据账户地址生成文件名
        //generate filename based on account address
        String fileName = accountKeyStoreDto.getAddress().concat(AccountConstant.ACCOUNTKEYSTORE_FILE_SUFFIX);
        //创建备份文件
        //create backup file
        backupFile = new File(backupFile, fileName);
        try {
            //如果文件不存在，则创建该文件
            //if the file does not exist, the file is created
            if (!backupFile.exists() && !backupFile.createNewFile()) {
                throw new NulsException(RpcErrorCode.FILE_OPERATION_FAILD);
            }
        } catch (IOException e) {
            throw new NulsException(RpcErrorCode.IO_ERROR,e);
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(backupFile);
            //convert keystore to JSON to store
            fileOutputStream.write(JSONUtils.obj2json(accountKeyStoreDto).getBytes());
        } catch (Exception e) {
            throw new NulsException(RpcErrorCode.PARSE_JSON_FAILD,e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.error(e);
                    throw new NulsException(RpcErrorCode.IO_ERROR,e);
                }
            }
        }
        //If it is a windows system path, remove the first /
        if (System.getProperties().getProperty(AccountConstant.OS_NAME).toUpperCase().indexOf(AccountConstant.OS_WINDOWS) != -1) {
            if (path.startsWith(AccountConstant.SLASH)) {
                path = path.substring(1);
            }
            path = path.replace(AccountConstant.SLASH, "\\");
        }
        String backupFileName = path + File.separator + fileName;
        return backupFileName;
    }

}
