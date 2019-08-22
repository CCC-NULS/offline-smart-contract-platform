package io.nuls.contract.rpc.resource.impl;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.nuls.base.RPCUtil;
import io.nuls.base.basic.AddressTool;
import io.nuls.base.data.Address;
import io.nuls.base.data.CoinData;
import io.nuls.base.data.NulsHash;
import io.nuls.base.signture.P2PHKSignature;
import io.nuls.base.signture.TransactionSignature;
import io.nuls.contract.account.model.bo.Account;
import io.nuls.contract.account.model.bo.AccountInfo;
import io.nuls.contract.account.model.bo.BalanceInfo;
import io.nuls.contract.account.model.bo.ContractInfo;
import io.nuls.contract.account.model.po.AccountKeyStoreDto;
import io.nuls.contract.autoconfig.ApiModuleInfoConfig;
import io.nuls.contract.constant.ContractConstant;
import io.nuls.contract.model.vo.AccountInfoVo;
import io.nuls.contract.model.vo.ChainInfo;
import io.nuls.contract.model.vo.ContractInfoVo;
import io.nuls.contract.account.utils.AccountTool;
import io.nuls.contract.helper.ContractTxHelper;
import io.nuls.contract.model.RpcErrorCode;
import io.nuls.contract.model.tx.CallContractTransaction;
import io.nuls.contract.model.tx.CreateContractTransaction;
import io.nuls.contract.model.tx.DeleteContractTransaction;
import io.nuls.contract.model.txdata.CallContractData;
import io.nuls.contract.model.txdata.CreateContractData;
import io.nuls.contract.model.txdata.DeleteContractData;
import io.nuls.contract.rpc.resource.OfflineContractResource;
import io.nuls.contract.service.*;
import io.nuls.contract.utils.ContractUtil;
import io.nuls.contract.utils.StringUtils;
import io.nuls.core.basic.Page;
import io.nuls.core.crypto.HexUtil;
import io.nuls.core.exception.NulsException;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.log.Log;
import io.nuls.core.model.FormatValidUtils;
import io.nuls.core.parse.JSONUtils;
import io.nuls.core.rockdb.util.DBUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@AutoJsonRpcServiceImpl
public class OfflineContractResourceImpl implements OfflineContractResource {

    @Autowired
    private ApiModuleInfoConfig infoConfig;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountKeyStoreService accountKeyStoreService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ContractTxHelper contractTxHelper;

    @Autowired
    private ChainService chainService;

    @Override
    public Map setProperty(String property, String value) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("result",false);
        if(StringUtils.isBlank(value)||StringUtils.isBlank(property)){
            Log.error("The values of the parameters \"property\" and \"value\" cannot be null");
            return map;
        }
        String setMethodName = "set" + property.substring(0,1).toUpperCase()+property.substring(1);
        String getMethodName = "get" + property.substring(0,1).toUpperCase()+property.substring(1);
        try{
            Method getMethod=infoConfig.getClass().getDeclaredMethod(getMethodName);
            Object oldVlue=getMethod.invoke(infoConfig);
            Method setMethod =  infoConfig.getClass().getDeclaredMethod(setMethodName, new Class[]{infoConfig.getClass().getDeclaredField(property).getType()});
            setMethod.invoke(infoConfig, new Object[]{StringUtils.getValueByType(value, infoConfig.getClass().getDeclaredField(property).getType())});
            map.put("result",true);
            Object newValue=getMethod.invoke(infoConfig);
            Log.info("The value of configuration parameter \""+property+"\" changed from "+oldVlue+" to "+newValue);
        }catch(Exception e){
            Log.error(e.getMessage());
        }
        return map;
    }



    @Override
    public Map createAccount(@JsonRpcParam(value = "chainId")int chainId, @JsonRpcParam(value = "password")String password) {
        Map<String,String> map = new HashMap<String,String>();
        try{
            Account account=accountService.createAccount(chainId,password);
            map.put("address",account.getAddress().toString());
        }catch (NulsException e){
            Log.error(e.getMessage());
            throw new NulsRuntimeException(e.getErrorCode());
        }
        return map;
    }

    @Override
    public boolean deleteAccount(int chainId, String address, String password) {
        boolean result=false;
        if (address == null) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"address");
        }
        if (!AddressTool.validAddress(chainId, address)) {
            throw new NulsRuntimeException(RpcErrorCode.ADDRESS_ERROR);
        }
        try {
             result=accountService.removeAccount(chainId,address,password);
        } catch (NulsException e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(e.getErrorCode());
        }
        return result;
    }

    @Override
    public AccountInfoVo getAccount(int chainId, int assetChainId, int assetId, String address) {
        if (address == null) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"address");
        }
        if (!AddressTool.validAddress(chainId, address)) {
            throw new NulsRuntimeException(RpcErrorCode.ADDRESS_ERROR);
        }
        Account account= null;
        try {
            account = accountService.getAccount(chainId,address);
        } catch (NulsException e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(e.getErrorCode());
        }

        if(account!=null){
            try {
                AccountInfoVo accountInfo= new AccountInfoVo();
                accountInfo.setChainId(account.getChainId());
                accountInfo.setAddress(account.getAddress().toString());
                AccountInfo accountForChain =accountService.getAccountForChain(chainId,address);
                accountInfo.setBalance(accountForChain.getBalance());
                accountInfo.setTotalBalance(accountForChain.getTotalBalance());
                accountInfo.setAlias(accountForChain.getAlias());
                return accountInfo;
            } catch (NulsException e) {
                Log.error(e.getMessage());
                throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
            }
        }else {
            throw new NulsRuntimeException(RpcErrorCode.ACCOUNT_NOT_EXIST);
        }

    }

    @Override
    public Page<AccountInfoVo> getAccountList(int chainId, int pageNumber, int pageSize) {
        Page<AccountInfoVo> resultPage;
        if (pageNumber < 1) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"pageNumber");
        }
        if ( pageSize < 1) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"pageSize");
        }

        List<AccountInfoVo> accountList= null;
        try {
            accountList = accountService.getAccountList(chainId);
        } catch (NulsException e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(e.getErrorCode());
        }
        int totalSize=0;
        if(accountList!=null){
            totalSize=accountList.size();
        }

        //根据分页参数返回账户地址列表 Returns the account address list according to paging parameters
        Page<AccountInfoVo> page = new Page<AccountInfoVo>(pageNumber, pageSize);
        page.setTotal(totalSize);
        int start = (pageNumber - 1) * pageSize;
        if (start >= totalSize) {
            return page;
        }

        int end = pageNumber * pageSize;
        if (end > totalSize) {
            end = totalSize;
        }
        accountList = accountList.subList(start, end);
        resultPage = new Page<>(page);
        resultPage.setList(accountList);
        return resultPage;
    }

    @Override
    public Map exportAccountKeyStore(int chainId, String address, String password, String filePath) {
        if (address == null ) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"address");
        }
/*        if (password == null ) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"password");
        }*/
        if (filePath == null ) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"filePath");
        }
        String backupFileName = null;
        try {
            backupFileName = accountKeyStoreService.backupAccountToKeyStore(filePath,chainId, address, password);
        } catch (NulsException e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(e.getErrorCode());
        }
        Map<String,String> map = new HashMap<String,String>();
        map.put("path",backupFileName);
        return map;
    }

    @Override
    public Map importAccountByKeystore(int chainId, String password, String keyStore, boolean overwrite) {
        /*if (password == null ) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"password");
        }*/
        if (keyStore == null) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"keyStore");
        }

        try {
            AccountKeyStoreDto accountKeyStoreDto = JSONUtils.json2pojo(new String(RPCUtil.decode(keyStore)), AccountKeyStoreDto.class);
            Account account=  accountService.importAccountByKeyStore(accountKeyStoreDto.toAccountKeyStore(), chainId, password, overwrite);
            Map<String,String> map = new HashMap<String,String>();
            map.put("address",account.getAddress().toString());
            return map;
        }catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode());
        }catch (IOException e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(e);
        }
    }

    @Override
    public Map importAccountByPriKey(int chainId, String priKey, String password, boolean overwrite) {
        if (priKey == null ) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"priKey");
        }
        /*if (password == null) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"password");
        }*/

        try {
            Account account= accountService.importAccountByPrikey(chainId, priKey, password, overwrite);
            Map<String,String> map = new HashMap<String,String>();
            map.put("address",account.getAddress().toString());
            return map;
        } catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode());
        }
    }

    @Override
    public Map exportPriKeyByAddress(int chainId, String address, String password) {
/*        if (password == null ) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"password");
        }*/
        if (address == null) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"address");
        }
        try {
            String unencryptedPrivateKey= accountService.getPrivateKey(chainId,address,password);
            Map<String,String> map = new HashMap<String,String>();
            map.put("privateKey",unencryptedPrivateKey);
            return map;
        } catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode());
        }
    }

    @Override
    public Map validateContractCreate(int chainId, String sender, long gasLimit, long price, String contractCode, Object[] args) {
        boolean isSuccess=true;
        try{
            isSuccess=contractService.validateContractCreate(chainId,sender,gasLimit,price,contractCode,args);
        }catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
        }
        Map<String, Boolean> params = new HashMap<>();
        params.put("success", isSuccess);
        return params;
    }

    @Override
    public Map uploadContractJar(int chainId,String jarFileData) {
        if (StringUtils.isBlank(jarFileData)) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"jarFileData");
        }
        Log.info("accept request,chainId="+chainId);
        String[] arr = jarFileData.split(",");
        if (arr.length != 2) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"jarFileData");
        }

        String body = arr[1];
        byte[] contractCode = Base64.getDecoder().decode(body);
        Map<String, Object> params = new HashMap<>();
        params.put("chainId", chainId);
        params.put("code", HexUtil.encode(contractCode));
        return params;
    }

    @Override
    public Map createContract(int chainId,int assetChainId,int assetId, String sender,String password, String contractCode,String alias, Object[] args, long gasLimit, long price,String remark) {
        if (gasLimit < 0) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"gasLimit");
        }

        if (price < 25) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"price not less than 25");
        }

        if (!AddressTool.validAddress(chainId, sender)) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"address");
        }
        if (StringUtils.isBlank(contractCode)) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"contractCode");
        }
        if(!FormatValidUtils.validAlias(alias)){
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"alias");
        }

        Account account= null;
        try {
            account = accountService.getAccount(chainId,sender);
        } catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode());
        }
        if(account==null){
            throw new NulsRuntimeException(RpcErrorCode.ACCOUNT_NOT_EXIST);
        }
        //账户密码验证
/*        boolean validate= accountService.validationPassword(chainId,sender,password);
        if(!validate){
            throw new NulsRuntimeException(RpcErrorCode.VALIADE_PW_ERROR);
        }*/

        if(account.isEncrypted()){
            if(!account.validatePassword(password)){
                throw new NulsRuntimeException(RpcErrorCode.VALIADE_PW_ERROR);
            }
        }


        byte[] contractCodeBytes = HexUtil.decode(contractCode);
        String[] argTypes=null;

        boolean isSuccess=true;
        try{
            argTypes=contractService.getContractConstructorArgsTypes(chainId,contractCode);
            if (argTypes==null){
                throw new NulsRuntimeException(RpcErrorCode.GET_CONSTRUSTOR_PARAMETER);
            }
            isSuccess=contractService.validateContractCreate(chainId,sender,gasLimit,price,contractCode,args);
        }catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
        }

        if(isSuccess){
            Address contract = AccountTool.createContractAddress(chainId);
            byte[] contractAddressBytes = contract.getAddressBytes();

            String contractAddress=AddressTool.getStringAddressByBytes( contract.getAddressBytes());
            byte[] senderBytes = AddressTool.getAddress(sender);

            CreateContractTransaction tx = new CreateContractTransaction();
           try{
               int gamLimit=contractService.imputedContractCreateGas(chainId,sender,contractCode,args);
                 if (StringUtils.isNotBlank(remark)) {
                     tx.setRemark(remark.getBytes(StandardCharsets.UTF_8));
                 }
                 tx.setTime(System.currentTimeMillis()/ 1000);

                 String[][] convertArgs= ContractUtil.twoDimensionalArray(args, argTypes);
                 //组装txData
                 CreateContractData createContractData= contractTxHelper.getCreateContractData(senderBytes,contractAddressBytes,BigInteger.ZERO,gamLimit,price,contractCodeBytes,alias,convertArgs);

                 // 计算CoinData
                 BalanceInfo balanceInfo=accountService.getAccountBalance(chainId,assetChainId,assetId,sender);
                 CoinData coinData = contractTxHelper.makeCoinData(chainId,assetId,senderBytes, contractAddressBytes,gamLimit,price,BigInteger.ZERO,tx.size(),createContractData,balanceInfo.getNonce(),balanceInfo.getBalance());
                 if(coinData==null){
                     throw new NulsRuntimeException(RpcErrorCode.INSUFFICIENT_BALANCE);
                 }

                 tx.setTxDataObj(createContractData);
                 tx.setCoinDataObj(coinData);
                 tx.serializeData();
                 tx.setHash(NulsHash.calcHash(tx.serializeForHash()));

               // 签名、发送交易到交易模块
                 P2PHKSignature signature = accountService.signDigest(tx.getHash().getBytes(),chainId,sender,password);
                 if (null == signature || signature.getSignData() == null) {
                     throw new NulsRuntimeException(RpcErrorCode.SIGNATURE_ERROR);
                 }
               TransactionSignature transactionSignature = new TransactionSignature();
               List<P2PHKSignature> p2PHKSignatures = new ArrayList<>();
               p2PHKSignatures.add(signature);
               transactionSignature.setP2PHKSignatures(p2PHKSignatures);
               tx.setTransactionSignature(transactionSignature.serialize());
               String txData = RPCUtil.encode(tx.serialize());
               boolean result= transactionService.broadcastTx(chainId,txData);
               if(result){
                   Map<String,String> map = new HashMap<String,String>();
                   map.put("contractAddress",contractAddress);
                   return map;
               }else {
                   throw new NulsRuntimeException(RpcErrorCode.BROADCAST_TX_ERROR);
               }
             }catch (NulsException e) {
               Log.error(e.format());
               throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
           }catch(NulsRuntimeException e){
               throw e;
           }catch (Throwable e){
               Log.error(e.getMessage());
               throw new NulsRuntimeException(RpcErrorCode.CONTRACT_TX_CREATE_ERROR);
           }
        }else {
            throw new NulsRuntimeException(RpcErrorCode.CONTRACT_VALIDATION_FAILED);
        }
    }

    @Override
    public Map getDefaultContractCode() {
        String filePath=infoConfig.getDefaultJarFilePath();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("haveJarFile",false);

        File jarFileDir=DBUtils.loadDataPath(filePath);
        if(jarFileDir.exists()){
            if(jarFileDir.isDirectory()){
                File files[] = jarFileDir.listFiles();
                for(File file:files){
                    if(file.getName().toLowerCase().endsWith(ContractConstant.FILE_EXT)){
                        try {
                            map.put("haveJarFile",true);
                            map.put("fileName",file.getName());
                            String  hexEncode=  ContractUtil.getContractCode(file);
                            map.put("codeHex",hexEncode);
                        } catch (Exception e) {
                            throw new NulsRuntimeException(RpcErrorCode.FILE_OPERATION_FAILD);
                        }
                        break;
                    }
                }
            }else{
                if(jarFileDir.getName().toLowerCase().endsWith(ContractConstant.FILE_EXT)){
                    try {
                        map.put("haveJarFile",true);
                        map.put("fileName",jarFileDir.getName());
                        String  hexEncode=  ContractUtil.getContractCode(jarFileDir);
                        map.put("codeHex",hexEncode);
                    } catch (Exception e) {
                        throw new NulsRuntimeException(RpcErrorCode.FILE_OPERATION_FAILD);
                    }
                }else{
                    throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"filePath");
                }
            }
        }
        return map;
    }

    @Override
    public Map imputedContractCreateGas(int chainId, String sender, String contractCode, Object[] args) {
        try {
            int gamLimit=contractService.imputedContractCreateGas(chainId,sender,contractCode,args);
            Map<String,Integer> map = new HashMap<String,Integer>();
            map.put("gasLimit",gamLimit);
            return map;
        } catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
        }
    }

    @Override
    public Map getContractConstructor(int chainId, String contractCode) {
        Log.info("accept request,chainId="+chainId);
        if (StringUtils.isBlank(contractCode)) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"contractCode");
        }
        try {
            Map result=contractService.getContractConstructor(chainId,contractCode);
            return result;
         } catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
        }
    }

    @Override
    public ContractInfoVo getContract(int chainId, String contractAddress) {
        if (!AddressTool.validAddress(chainId, contractAddress)) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"contractAddress");
        }
        try {
            ContractInfo contractInfo=contractService.getContract(chainId,contractAddress);
            if(contractInfo!=null){
                return contractInfo.toContractInfoVo();
            }else {
                throw new NulsRuntimeException(RpcErrorCode.GET_CONTRACT_INFO_FAILED);
            }
        } catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
        }
    }

    @Override
    public Map callContract(int chainId, int assetChainId,int assetId, String sender,String password, String contractAddress, BigInteger value, String methodName, String methodDesc, Object[] args, long gasLimit, long price,String remark) {
        if (value.compareTo(BigInteger.ZERO) < 0) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"value");
        }
        if (gasLimit < 0) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"gasLimit");
        }

        if (price < 25) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"price not less than 25");
        }

        if (!AddressTool.validAddress(chainId, sender)) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"sender's address");
        }
        if (!AddressTool.validAddress(chainId, contractAddress)) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"contractAddress");
        }
        if (StringUtils.isBlank(methodName)) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"methodName");
        }

        //账户密码验证
        boolean validate= accountService.validationPassword(chainId,sender,password);
        if(!validate){
            throw new NulsRuntimeException(RpcErrorCode.VALIADE_PW_ERROR);
        }
        byte[] contractAddressBytes = AddressTool.getAddress(contractAddress);
        String[] argsTypes=null;
        try{
            argsTypes= contractService.getContractMethodArgsTypes(chainId, contractAddress, methodName);
        }catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
        }
        String[][] convertArgs = ContractUtil.twoDimensionalArray(args, argsTypes);
        try{
            validate=contractService.validateContractCall(chainId,sender,value,gasLimit,price,contractAddress,methodName,methodDesc,args);
        }catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(RpcErrorCode.VALIADE_CONTRACT_CALL_ERROR,e.getMessage());
        }
        if(!validate){
            throw new NulsRuntimeException(RpcErrorCode.VALIADE_CONTRACT_CALL_ERROR);
        }

        CallContractTransaction tx = new CallContractTransaction();
        if (StringUtils.isNotBlank(remark)) {
            tx.setRemark(remark.getBytes(StandardCharsets.UTF_8));
        }
        tx.setTime(System.currentTimeMillis()/ 1000);
        byte[] senderBytes = AddressTool.getAddress(sender);

        try{
            //组装txData
            CallContractData createContractData= contractTxHelper.getCallContractData(senderBytes, contractAddressBytes,value,gasLimit,price,methodName,methodDesc, convertArgs);

            BalanceInfo balanceInfo=accountService.getAccountBalance(chainId,assetChainId,assetId,sender);
            CoinData coinData = contractTxHelper.makeCoinData(chainId,assetId,senderBytes, contractAddressBytes,gasLimit,price,value,tx.size(),createContractData,balanceInfo.getNonce(),balanceInfo.getBalance());
            if(coinData==null){
                throw new NulsRuntimeException(RpcErrorCode.INSUFFICIENT_BALANCE);
            }
            tx.setTxDataObj(createContractData);
            tx.setCoinDataObj(coinData);

            tx.serializeData();
            tx.setHash(NulsHash.calcHash(tx.serializeForHash()));

            // 签名、发送交易到交易模块
            P2PHKSignature signature = accountService.signDigest(tx.getHash().getBytes(),chainId,sender,password);
            if (null == signature || signature.getSignData() == null) {
                throw new NulsRuntimeException(RpcErrorCode.SIGNATURE_ERROR);
            }
            TransactionSignature transactionSignature = new TransactionSignature();
            List<P2PHKSignature> p2PHKSignatures = new ArrayList<>();
            p2PHKSignatures.add(signature);
            transactionSignature.setP2PHKSignatures(p2PHKSignatures);
            tx.setTransactionSignature(transactionSignature.serialize());
            String txData = RPCUtil.encode(tx.serialize());
            boolean result= transactionService.broadcastTx(chainId,txData);
            if(result){
                Map<String,String> map = new HashMap<String,String>();
                map.put("txHash",tx.getHash().toHex());
                return map;
            }else {
                throw new NulsRuntimeException(RpcErrorCode.BROADCAST_TX_ERROR);
            }
        }catch(NulsRuntimeException e){
            throw e;
        }catch (Throwable e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(e);
        }
    }

    @Override
    public Map deleteContract(int chainId,int assetChainId, int assetId, String sender,String password, String contractAddress,String remark) {
        if (!AddressTool.validAddress(chainId, sender)) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"sender's address");
        }
        //账户密码验证
        boolean validate= accountService.validationPassword(chainId,sender,password);
        if(!validate){
            throw new NulsRuntimeException(RpcErrorCode.VALIADE_PW_ERROR);
        }
        try{
            validate =contractService.validateContractDelete(chainId, sender, contractAddress);
        }catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
        }

        if(!validate){
            throw new NulsRuntimeException(RpcErrorCode.VALIADE_CONTRACT_DELETE_ERROR);
        }
        byte[] contractAddressBytes = AddressTool.getAddress(contractAddress);
        DeleteContractTransaction tx = new DeleteContractTransaction();
        if (StringUtils.isNotBlank(remark)) {
            tx.setRemark(remark.getBytes(StandardCharsets.UTF_8));
        }
        tx.setTime(System.currentTimeMillis()/ 1000);
        byte[] senderBytes = AddressTool.getAddress(sender);
        try {
            DeleteContractData deleteContractData= contractTxHelper.getDeleteContractData(contractAddressBytes,senderBytes);
            BalanceInfo balanceInfo=accountService.getAccountBalance(chainId,assetChainId,assetId,sender);
            CoinData coinData = contractTxHelper.makeCoinData(chainId,assetId,senderBytes, contractAddressBytes,0L, 0L, BigInteger.ZERO,tx.size(),deleteContractData,balanceInfo.getNonce(),balanceInfo.getBalance());
            if(coinData==null){
                throw new NulsRuntimeException(RpcErrorCode.INSUFFICIENT_BALANCE);
            }
            tx.setTxDataObj(deleteContractData);
            tx.setCoinDataObj(coinData);

            tx.serializeData();
            tx.setHash(NulsHash.calcHash(tx.serializeForHash()));

            // 签名、发送交易到交易模块
            P2PHKSignature signature = accountService.signDigest(tx.getHash().getBytes(),chainId,sender,password);
            if (null == signature || signature.getSignData() == null) {
                throw new NulsRuntimeException(RpcErrorCode.SIGNATURE_ERROR);
            }
            TransactionSignature transactionSignature = new TransactionSignature();
            List<P2PHKSignature> p2PHKSignatures = new ArrayList<>();
            p2PHKSignatures.add(signature);
            transactionSignature.setP2PHKSignatures(p2PHKSignatures);
            tx.setTransactionSignature(transactionSignature.serialize());
            String txData = RPCUtil.encode(tx.serialize());
            boolean result= transactionService.broadcastTx(chainId,txData);
            if(result){
                Map<String,String> map = new HashMap<String,String>();
                map.put("address",contractAddress);
                return map;
            }else {
                throw new NulsRuntimeException(RpcErrorCode.BROADCAST_TX_ERROR);
            }
        }catch(NulsRuntimeException e){
            throw e;
        }catch (Throwable e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(e);
        }
    }

    @Override
    public Map invokeContractViewMethod(int chainId, String contractAddress, String methodName, String methodDesc, Object[] args) {
        if (!AddressTool.validAddress(chainId, contractAddress)) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"contractAddress");
        }
        if (StringUtils.isBlank(methodName)) {
            throw new NulsRuntimeException(RpcErrorCode.NULL_PARAMETER,"methodName");
        }
        try {
            String invokeResult= contractService.invokeView(chainId,contractAddress,methodName,methodDesc,args);
            Map<String,String> map = new HashMap<String,String>();
            map.put("methodReturn",invokeResult);
            return map;
        }catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
        }
    }

    @Override
    public Map getContractMethodArgsTypes(int chainId, String contractAddress, String methodName) {
        if (!AddressTool.validAddress(chainId, contractAddress)) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR,"contractAddress");
        }
        String[] argsTypes=null;
        try{
            argsTypes= contractService.getContractMethodArgsTypes(chainId, contractAddress, methodName);
        }catch (NulsException e) {
            Log.error(e.format());
            throw new NulsRuntimeException(e.getErrorCode(),e.getMessage());
        }
        Map<String,String[]> map = new HashMap<String,String[]>();
        map.put("argsTypes",argsTypes);
        return map;
    }

}
