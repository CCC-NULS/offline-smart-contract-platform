package io.nuls.contract.rpc.resource;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.nuls.contract.model.vo.AccountInfoVo;
import io.nuls.contract.model.vo.ChainInfo;
import io.nuls.contract.model.vo.ContractInfoVo;
import io.nuls.core.basic.Page;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@JsonRpcService("/offlineSmartContract")
public interface OfflineContractResource {

    public Map getVersionInfo();

    public Map setProperty(@JsonRpcParam(value = "property")String property,@JsonRpcParam(value = "value")String value);

    public Map createAccount(@JsonRpcParam(value = "chainId")int chainId, @JsonRpcParam(value = "password")String password);

    public boolean deleteAccount(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "address")String address, @JsonRpcParam(value = "password")String password);
    public AccountInfoVo getAccount(@JsonRpcParam(value = "chainId")int chainId, @JsonRpcParam(value = "assetChainId")int assetChainId, @JsonRpcParam(value = "assetId")int assetId, @JsonRpcParam(value = "address")String address);

    public Page<AccountInfoVo> getAccountList(@JsonRpcParam(value = "chainId")int chainId, @JsonRpcParam(value = "pageNumber")int pageNumber, @JsonRpcParam(value = "pageSize")int pageSize);

    public Map exportAccountKeyStore(@JsonRpcParam(value = "chainId")int chainId, @JsonRpcParam(value = "address")String address, @JsonRpcParam(value = "password")String password,@JsonRpcParam(value = "filePath")String filePath);

    public Map importAccountByKeystore(@JsonRpcParam(value = "chainId")int chainId, @JsonRpcParam(value = "password")String password,@JsonRpcParam(value = "keyStore")String keyStore,@JsonRpcParam(value = "overwrite")boolean overwrite);

    public Map importAccountByPriKey(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "priKey")String priKey, @JsonRpcParam(value = "password")String password, @JsonRpcParam(value = "overwrite")boolean overwrite);

    public Map exportPriKeyByAddress(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "address")String address,@JsonRpcParam(value = "password")String password);

    public Map validateContractCreate(@JsonRpcParam(value = "chainId")int chainId, @JsonRpcParam(value = "sender")String sender, @JsonRpcParam(value = "gasLimit")long gasLimit, @JsonRpcParam(value = "price")long price, @JsonRpcParam(value = "contractCode")String contractCode, @JsonRpcParam(value = "args")Object[] args);

    public Map uploadContractJar(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "jarFileData")String jarFileData);

    public Map createContract(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "assetChainId")int assetChainId, @JsonRpcParam(value = "assetId")int assetId, @JsonRpcParam(value = "sender")String sender, @JsonRpcParam(value = "password")String password, @JsonRpcParam(value = "contractCode")String contractCode, @JsonRpcParam(value = "alias")String alias, @JsonRpcParam(value = "args")Object[] args, @JsonRpcParam(value = "gasLimit")long gasLimit, @JsonRpcParam(value = "price")long price, @JsonRpcParam(value = "remark")String remark);

    public Map getDefaultContractCode();

    public Map imputedContractCreateGas(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "sender")String sender,@JsonRpcParam(value = "contractCode")String contractCode,@JsonRpcParam(value = "args")Object[] args);

    public Map getContractConstructor(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "contractCode")String contractCode);

    public ContractInfoVo getContract(@JsonRpcParam(value = "chainId")int chainId, @JsonRpcParam(value = "contractAddress")String contractAddress);

    public Map callContract(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "assetChainId")int assetChainId, @JsonRpcParam(value = "assetId")int assetId, @JsonRpcParam(value = "sender")String sender,@JsonRpcParam(value = "password")String password, @JsonRpcParam(value = "contractAddress")String contractAddress,@JsonRpcParam(value = "value")BigInteger value,@JsonRpcParam(value = "methodName")String methodName, @JsonRpcParam(value = "methodDesc")String methodDesc,@JsonRpcParam(value = "args")Object[] args, @JsonRpcParam(value = "gasLimit")long gasLimit, @JsonRpcParam(value = "price")long price,@JsonRpcParam(value = "remark")String remark);

    public Map deleteContract(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "assetChainId")int assetChainId, @JsonRpcParam(value = "assetId")int assetId, @JsonRpcParam(value = "sender")String sender,@JsonRpcParam(value = "password")String password, @JsonRpcParam(value = "contractAddress")String contractAddress,@JsonRpcParam(value = "remark")String remark);

    public Map  invokeContractViewMethod(@JsonRpcParam(value = "chainId")int chainId, @JsonRpcParam(value = "contractAddress")String contractAddress, @JsonRpcParam(value = "methodName")String methodName, @JsonRpcParam(value = "methodDesc")String methodDesc, @JsonRpcParam(value = "args")Object[] args);

    public Map getContractMethodArgsTypes(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "contractAddress")String contractAddress, @JsonRpcParam(value = "methodName")String methodName);

    public Map getContractExecuteResultInfo(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "txHash")String txHash);

    public Map getContractExecuteArgsInfo(@JsonRpcParam(value = "chainId")int chainId,@JsonRpcParam(value = "txHash")String txHash,@JsonRpcParam(value = "txType")int txType);

}
