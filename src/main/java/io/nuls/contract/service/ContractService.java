package io.nuls.contract.service;

import io.nuls.contract.account.model.bo.ContractInfo;
import io.nuls.contract.model.ContractResultInfo;
import io.nuls.contract.model.deserialization.ContractResultDataDto;
import io.nuls.core.exception.NulsException;

import java.math.BigInteger;
import java.util.Map;

public interface ContractService {

    public String[] getContractConstructorArgsTypes(int chainId,String contractCode) throws NulsException;

    public Map getContractConstructor(int chainId,String contractCode) throws NulsException;

    public boolean validateContractCreate(int chainId,String sender,long gasLimit,long price,String contractCode,Object[] args) throws NulsException;

    public int imputedContractCreateGas(int chainId,String sender,String contractCode,Object[] args) throws NulsException;

    public String[] getContractMethodArgsTypes(int chainId,String contractAddress,String methodname) throws NulsException;

    public boolean validateContractCall(int chainId, String sender, BigInteger value,long gasLimit,long price,String contractAddress,String methodName,String methodDesc,Object[] args) throws NulsException;

    public int imputedContractCallGas(int chainId ,String sender,BigInteger  value,String contractAddress,String methodName,String methodDesc,Object[] args) throws NulsException;

    public boolean validateContractDelete(int chainId ,String sender,String contractAddress) throws NulsException;

    public String invokeView(int chainId, Object contractAddress, Object methodName, Object methodDesc, Object args) throws NulsException;

    public ContractInfo getContract(int chainId, String contractAddress) throws NulsException;

    public ContractResultDataDto getContractTxResult(int chainId, String txHash) throws NulsException;
}
